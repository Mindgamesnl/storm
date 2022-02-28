package com.craftmend.storm.utils;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.ColumnType;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import com.craftmend.storm.parser.ModelParser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reflection {

    public static String TableNameFromClass(Class<? extends StormModel> modelClazz) {
        if (!modelClazz.isAnnotationPresent(Table.class)) {
            // auto complete model name
            return CaseConverter.camelToSnake(modelClazz.getSimpleName());
        }
        return modelClazz.getAnnotation(Table.class).name();
    }

    public static String getAnnotatedFieldName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            String declared = field.getAnnotation(Column.class).name();
            if (!declared.equals("")) {
                return declared;
            }
        }
        return CaseConverter.camelToSnake(field.getName());
    }

    public static ModelParser<? extends StormModel> getAnnotatedReference(Storm storm, Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Class<? extends StormModel>[] references = field.getAnnotation(Column.class).references();
            if (references.length == 1) {
                return storm.getParsedModel(references[0], false);
            }
        }
        throw new IllegalArgumentException("One reference model was expected");
    }

    public static KeyType getAnnotatedKeyType(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).keyType();
        }
        return KeyType.NONE;
    }

    public static boolean getAnnotatedAutoIncrement(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).autoIncrement();
        }
        return false;
    }

    public static String getAnnotatedMatchTo(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            String d = field.getAnnotation(Column.class).matchTo();
            if (d.equals("")) return null;
            return d;
        }
        return null;
    }

    public static String getAnnotatedDefaultValue(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            String d = field.getAnnotation(Column.class).defaultValue();
            if (d.equals("")) return null;
            return d;
        }
        return null;
    }

    public static boolean getAnnotatedNotNull(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).notNull();
        }
        return false;
    }

    public static ColumnType getAnnotatedColumnType(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).type();
        }
        return ColumnType.VALUE;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    public static boolean getAnnotatedUnique(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).unique();
        }
        return false;
    }

    public static int getAnnotatedFieldMax(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).length();
        }
        return 255;
    }

}
