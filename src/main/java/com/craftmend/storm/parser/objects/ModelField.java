package com.craftmend.storm.parser.objects;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.parser.types.TypeRegistry;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ModelField {

    @Getter private Class type;
    @Getter private String javaFieldName;
    @Getter private String columnName;
    @Getter private StormTypeAdapter<?> adapter;
    @Getter private Class<? extends StormModel> model;
    @Getter private int max;
    @Getter private KeyType keyType;
    @Getter private boolean unique;
    @Getter private boolean autoIncrement;
    @Getter private boolean notNull;
    @Getter private String defaultValue;
    private Field reflectedField;

    public ModelField(Class<? extends StormModel> modelClass, Field field) {
        this.model = modelClass;
        this.type = field.getType();
        this.javaFieldName = field.getName();
        this.columnName = Reflection.getAnnotatedFieldName(field);
        this.adapter = TypeRegistry.getAdapterFor(this.type);
        this.max = Reflection.getAnnotatedFieldMax(field);
        this.keyType = Reflection.getAnnotatedKeyType(field);
        this.unique = Reflection.getAnnotatedUnique(field);
        this.autoIncrement = Reflection.getAnnotatedAutoIncrement(field);
        this.notNull = Reflection.getAnnotatedNotNull(field);
        this.defaultValue = Reflection.getAnnotatedDefaultValue(field);
        this.reflectedField = field;
    }

    @SneakyThrows
    public Object valueOn(StormModel model) {
        this.reflectedField.setAccessible(true);
        return this.reflectedField.get(model);
    }

    public String buildSqlType() {
        // build value
        String sqlTypeDeclaration = this.adapter.getSqlBaseType();
        sqlTypeDeclaration = sqlTypeDeclaration.replace("%max", this.max + "");
        return this.columnName + " " + sqlTypeDeclaration +
                (this.keyType == KeyType.PRIMARY ? " PRIMARY KEY" : "") +
                (this.autoIncrement ? " AUTOINCREMENT" : "") +
                (this.defaultValue != null ? " DEFAULT(" +
                        (adapter.escapeAsString() ? "'" + this.defaultValue + "'" : this.defaultValue)
                        + ")" : "") +
                (this.notNull ? " NOT NULL" : "") +
                (this.unique ? " UNIQUE" : "");
    }

}
