package com.craftmend.storm.parser;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.ColumnType;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.objects.RelationField;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModelParser<T extends StormModel> {

    @Getter @Setter private boolean migrated = false;
    @Getter private final String tableName;
    @Getter private final ParsedField[] parsedFields;
    @Getter private List<RelationField> relationFields = new ArrayList<>();
    @Getter private Class<T> ownType;
    @Getter private Storm storm;
    @Getter private T emptyInstance;

    public ModelParser(Class<T> modelClazz, Storm storm, T emptyInstance) {
        this.tableName = Reflection.TableNameFromClass(modelClazz);
        this.ownType = modelClazz;
        this.storm = storm;
        this.emptyInstance = emptyInstance;

        // load fields
        List<ParsedField> tempFields = new ArrayList<>();
        for (Field declaredField : Reflection.getAllFields(new ArrayList<>(), modelClazz)) {
            if (declaredField.isAnnotationPresent(Column.class)) {
                if (Reflection.getAnnotatedColumnType(declaredField) == ColumnType.VALUE) {
                    tempFields.add(new ParsedField(storm, modelClazz, declaredField.getType(), declaredField));
                } else {
                    relationFields.add(new RelationField(storm, modelClazz, declaredField.getType(), declaredField));
                }
            }
        }
        parsedFields = new ParsedField[tempFields.size()];
        tempFields.toArray(parsedFields);
    }

    public ParsedField fieldByColumnName(String columnName) {
        for (ParsedField parsedField : parsedFields) {
            if (parsedField.getColumnName().equals(columnName)) return parsedField;
        }
        return null;
    }

    public T fromResultSet(ResultSet resultSet, List<RelationField> relationFields) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T emptySelf = ownType.getConstructor().newInstance();

        // map normal fields
        for (ParsedField parsedField : parsedFields) {
            Object value = parsedField.getAdapter().fromSql(resultSet.getObject(parsedField.getColumnName()));
            parsedField.getReflectedField().setAccessible(true);
            parsedField.getReflectedField().set(emptySelf, value);
        }

        // polyfill children
        for (RelationField relationField : relationFields) {
            try {
                Collection<?> childValues = (Collection<?>) storm
                        .buildQuery(relationField.getTargetParser().ownType)
                        .where(relationField.getMatchToField(), Where.EQUAL, emptySelf.getId())
                        .execute()
                        .join();
                relationField.getReflectedField().setAccessible(true);
                List<Object> list = new ArrayList<>();
                for (Object childValue : childValues) {
                    list.add(childValue);
                }
                relationField.getReflectedField().set(emptySelf, list);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("Couldn't fulfil child values for " + relationField.getModel().getName() + "." + relationField.getReflectedField().getName() + ". Error: " + e.getMessage());
            }
        }

        return emptySelf;
    }

}
