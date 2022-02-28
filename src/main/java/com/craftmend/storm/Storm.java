package com.craftmend.storm;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.builders.QueryBuilder;
import com.craftmend.storm.connection.StormDriver;
import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.parser.objects.ModelField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class Storm {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final Map<Class<? extends StormModel>, ModelParser<? extends StormModel>> registeredModels = new HashMap<>();
    private final StormDriver driver;

    /**
     * Initialize a new STORM instance with a given database driver
     * @param driver Database driver
     */
    public Storm(StormDriver driver) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        this.driver = driver;
    }

    /**
     * Storm must register/migrate models before they can be used internally.
     * It registers the parsed class definition locally, but also plays a vital role in schema management.
     *
     * It checks the remote if a table exists with the annotated name, and creates it if it doesn't (along with the
     *          field schema for all annotated columns)
     *
     * It also checks the local schema class against the database, and alters the database table to add/remove
     * values based on dynamic code changes.
     *
     * @param model Model to register
     * @throws SQLException Something went boom
     */
    public void migrate(StormModel model) throws SQLException {
        if (registeredModels.containsKey(model.getClass())) return;
        ModelParser<?> parsed = new ModelParser(model.getClass());

        logger.info("Registering class <-> table (" + parsed.getTableName() +" <->" + model.getClass().getSimpleName() + ".java)");

        try (ResultSet tables = driver.getMeta().getTables(null, null, parsed.getTableName(), null)) {
            if (!tables.next()) {
                // table doesn't exist.. creating
                logger.info("Creating table " + parsed.getTableName() + "...");
                driver.execute(model.statements().buildSqlTableCreateStatement(driver.getDialect()));
            }
        }

        // find fields in table
        Map<String, String> columnsInDatabase = new HashMap<>();
        try (ResultSet tables = driver.getMeta().getColumns(null, null, parsed.getTableName(), null)) {
            while(tables.next()) {
                String type = tables.getString("TYPE_NAME");
                String name = tables.getString("COLUMN_NAME");
                columnsInDatabase.put(name, type);
            }
        }

        // compare local tables to the ones in the database, we might need to add, remove or update some
        Set<String> missingInDatabase = new HashSet<>();
        for (ModelField parsedField : parsed.getParsedFields()) {
            missingInDatabase.add(parsedField.getColumnName());
        }
        missingInDatabase.removeAll(columnsInDatabase.keySet());

        // compare local
        Set<String> missingInLocal = new HashSet<>();
        missingInLocal.addAll(columnsInDatabase.keySet());
        for (ModelField parsedField : parsed.getParsedFields()) {
            missingInLocal.remove(parsedField.getColumnName());
        }

        // drop remote fields
        for (String columnName : missingInLocal) {
            logger.warning("Dropping column '" + columnName + "' because it's not present in the local class");
            driver.executeUpdate("ALTER TABLE %table DROP COLUMN %column;"
                    .replace("%table", parsed.getTableName())
                    .replace("%column", columnName));
        }

        // add remote fields
        for (String columnName : missingInDatabase) {
            // find type
            for (ModelField parsedField : parsed.getParsedFields()) {
                if (parsedField.getColumnName().equals(columnName)) {
                    logger.warning("Column '" + columnName + "' is not present in the remote table schema. Altering table and adding type " + driver.getDialect().compileColumn(parsedField));
                    String statement = "ALTER TABLE %table ADD COLUMN %columnName %columnData;"
                            .replace("%table", parsed.getTableName())
                            .replace("%columnName", columnName)
                            .replace("%columnData", driver.getDialect().compileColumn(parsedField));
                    driver.executeUpdate(statement);
                }
            }
        }

        registeredModels.put(model.getClass(), parsed);
    }

    /**
     * @param model Start a new query
     * @return Query for you to play with
     */
    public <T extends StormModel> QueryBuilder<T> buildQuery(Class<T> model) {
        ModelParser<T> parser = (ModelParser<T>) registeredModels.get(model);
        if (parser == null) throw new IllegalArgumentException("The model " + model.getName() + " isn't loaded. Please call storm.migrate() with an empty instance");
        return new QueryBuilder<>(model, parser, this);
    }

    /**
     * Execute query
     *
     * @param query Query to execute
     * @return Processed result set
     * @throws Exception
     */
    public <T extends StormModel> CompletableFuture<Collection<T>> executeQuery(QueryBuilder<T> query) throws Exception {
        CompletableFuture<Collection<T>> future = new CompletableFuture<>();
        HashSet<T> results = new HashSet<>();
        ModelParser<T> parser = (ModelParser<T>) registeredModels.get(query.getModel());
        if (parser == null) throw new IllegalArgumentException("The model " + query.getModel().getName() + " isn't loaded. Please call storm.migrate() with an empty instance");
        QueryBuilder.PreparedQuery pq = query.build();
        driver.executeQuery(pq.getQuery(), rows -> {
            while (rows.next()) {
                results.add(parser.fromResultSet(rows));
            }
            future.complete(results);
        }, pq.getValues());

        return future;
    }

    /**
     * Get *ALL* models that are stored for a defined type.
     * This might be really memory intensive for large data sets.
     *
     * @param model Model to check
     * @return A promise with the results
     * @throws Exception
     */
    public <T extends StormModel> CompletableFuture<Collection<T>> findAll(Class<T> model) throws Exception {
        CompletableFuture<Collection<T>> future = new CompletableFuture<>();
        HashSet<T> results = new HashSet<>();
        ModelParser<T> parser = (ModelParser<T>) registeredModels.get(model);
        if (parser == null) throw new IllegalArgumentException("The model " + model.getName() + " isn't loaded. Please call storm.migrate() with an empty instance");

        driver.executeQuery("select * from " + parser.getTableName(), rows -> {
            while (rows.next()) {
                results.add(parser.fromResultSet(rows));
            }
            future.complete(results);
        });

        return future;
    }

    /**
     * @param model Delete a row
     * @throws SQLException
     */
    public void delete(StormModel model) throws SQLException {
        ModelParser parser = registeredModels.get(model.getClass());
        if (parser == null) throw new IllegalArgumentException("The model " + model.getClass().getName() + " isn't loaded. Please call storm.migrate() with an empty instance");
        if (model.getId() == null) throw new IllegalArgumentException("This model doesn't have an ID");
        driver.executeUpdate("DELETE FROM " + parser.getTableName() + " WHERE id=" + model.getId());
    }

    /**
     * Save the model in the database.
     * This either inserts it as a new row, or updates an existing row if there already is a row with this ID
     * @param model Target to save
     * @return int Returns the amount of effected rows
     */
    public int save(StormModel model) throws SQLException {
        String updateOrInsert = "update %tableName set %psUpdateValues where id=%id";
        String insertStatement = "insert into %tableName(%insertVars) values(%insertValues);";

        // ps update value things
        StringBuilder updateValues = new StringBuilder();
        StringBuilder insertRow = new StringBuilder();
        String insertPointers = "";
        int nonAutoFields = model.parsed().getParsedFields().length;
        for (ModelField parsedField : model.parsed().getParsedFields()) {
            if (parsedField.isAutoIncrement()) {
                nonAutoFields--;
            }
        }
        Object[] preparedValues = new Object[nonAutoFields];
        int pvi = 0;
        for (int i = 0; i < model.parsed().getParsedFields().length; i++) {
            boolean notLast = (i+1) != nonAutoFields;
            ModelField mf = model.parsed().getParsedFields()[i];
            if (mf.isAutoIncrement()) {
                // skip auto fields
                continue;
            }
            preparedValues[pvi] = mf.valueOn(model);
            pvi++;
            updateValues.append(mf.getColumnName() + " = ?");
            if (notLast) {
                updateValues.append(", ");
            }

            insertRow.append(mf.getColumnName());
            insertPointers += "?";
            if (notLast) {
                insertRow.append(", ");
                insertPointers += ", ";
            }
        }

        insertStatement = insertStatement
                .replace("%insertVars", insertRow.toString())
                .replace("%insertValues", insertPointers)
                .replaceAll("%tableName", model.parsed().getTableName());

        updateOrInsert = updateOrInsert
                .replace("%psUpdateValues", updateValues.toString())
                .replaceAll("%tableName", model.parsed().getTableName())
                .replace("%id", model.getId() + "");


        if (model.getId() == null) {
            return driver.executeUpdate(insertStatement, preparedValues);
        } else {
            return driver.executeUpdate(updateOrInsert, preparedValues);
        }
    }

}
