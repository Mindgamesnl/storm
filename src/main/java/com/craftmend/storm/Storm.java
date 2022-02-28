package com.craftmend.storm;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.connection.StormDriver;
import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.parser.objects.ModelField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Storm {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());
    private Map<Class<? extends StormModel>, ModelParser> registeredModels = new HashMap<>();
    private StormDriver driver;

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
        ModelParser parsed = new ModelParser(model.getClass());

        logger.info("Registering class <-> table (" + parsed.getTableName() +" <->" + model.getClass().getSimpleName() + ".java)");

        try (ResultSet tables = driver.getMeta().getTables(null, null, parsed.getTableName(), null)) {
            if (!tables.next()) {
                // table doesn't exist.. creating
                logger.info("Creating table " + parsed.getTableName() + "...");
                driver.execute(model.statements().buildSqlTableCreateStatement());
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
                    logger.warning("Column '" + columnName + "' is not present in the remote table schema. Altering table and adding type " + parsedField.buildSqlType());
                    String statement = "ALTER TABLE %table ADD COLUMN %columnName %columnData;"
                            .replace("%table", parsed.getTableName())
                            .replace("%columnName", columnName)
                            .replace("%columnData", parsedField.buildSqlType());
                    driver.executeUpdate(statement);
                }
            }
        }

        registeredModels.put(model.getClass(), parsed);
    }

    /**
     * Save the model in the database.
     * This either inserts it as a new row, or updates an existing row if there already is a row with this ID
     * @param model Target to save
     */
    public void save(StormModel model) throws SQLException {

    }

}
