package com.craftmend.storm.api;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.builders.StatementBuilder;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.ModelParser;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Core of any storm model, has required api parsing flags
 */
public abstract class BaseStormModel {

    private ModelParser<? extends BaseStormModel> parsedSelf;
    private StatementBuilder statementBuilder;

    public ModelParser<? extends BaseStormModel> parsed(Storm orm) {
        if (parsedSelf != null) return parsedSelf;
        parsedSelf = new ModelParser(getClass(), orm, this);
        return parsedSelf;
    }

    public StatementBuilder statements() {
        if (statementBuilder != null) return statementBuilder;
        statementBuilder = new StatementBuilder(this);
        return statementBuilder;
    }

    /**
     * Gets the primary key field
     * by looking for the field annotated with the KeyType.PRIMARY keytype
     */
    public Field getPkField() {
        return Arrays.stream(this.getClass().getFields())
                .filter(fieldEntry -> {
                    Column annotation = fieldEntry.getAnnotation(Column.class);
                    return annotation.keyType() == KeyType.PRIMARY;
                }).findAny().orElse(null);
    }

    /**
     * Gets the primary key's database column name
     */
    public String getPkFieldName() {
        Field field = getPkField();
        Column annotation = field.getAnnotation(Column.class);
        return annotation.name().isEmpty() ? field.getName() : annotation.name();
    }

    /**
     * Gets the primary key value of the model instance
     * by finding the field annotated with the KeyType.PRIMARY keytype
     */
    public Object getPk() {
        Field field = getPkField();
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            // shouldn't happen as it's within this class
            throw new RuntimeException(e);
        }
    }

    public void setPk(Object pk) {
        Field field = getPkField();
        try {
            field.set(this, pk);
        } catch (IllegalAccessException e) {
            // shouldn't happen as it's within this class
            throw new RuntimeException(e);
        }
    }

    public void preSave() {}
    public void preDelete() {}
    public void postSave() {}
    public void postDelete() {}

}
