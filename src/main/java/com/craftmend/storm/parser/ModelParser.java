package com.craftmend.storm.parser;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.objects.ModelField;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelParser<T extends StormModel> {

    @Getter private final String tableName;
    @Getter private final ModelField[] parsedFields;
    private Class<T> ownType;

    public ModelParser(Class<T> modelClazz) {
        this.tableName = Reflection.TableNameFromClass(modelClazz);
        this.ownType = modelClazz;

        // load fields
        List<ModelField> tempFields = new ArrayList<>();
        for (Field declaredField : Reflection.getAllFields(new ArrayList<>(), modelClazz)) {
            if (declaredField.isAnnotationPresent(Column.class)) {
                tempFields.add(new ModelField(modelClazz, declaredField.getType(), declaredField));
            }
        }
        parsedFields = new ModelField[tempFields.size()];
        tempFields.toArray(parsedFields);
    }

    public ModelField fieldByColumnName(String columnName) {
        for (ModelField parsedField : parsedFields) {
            if (parsedField.getColumnName().equals(columnName)) return parsedField;
        }
        return null;
    }

    public T fromResultSet(ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T emptySelf = ownType.getConstructor().newInstance();

        for (ModelField parsedField : parsedFields) {
            Object value = parsedField.getAdapter().fromSql(resultSet.getObject(parsedField.getColumnName()));
            parsedField.getReflectedField().setAccessible(true);
            parsedField.getReflectedField().set(emptySelf, value);
        }

        return emptySelf;
    }

}
