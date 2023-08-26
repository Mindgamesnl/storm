package com.craftmend.storm.parser.objects;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.BaseStormModel;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.parser.types.TypeRegistry;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ParsedField<T> {

    @Getter private Class type;
    @Getter private String javaFieldName;
    @Getter private String columnName;
    @Getter private StormTypeAdapter<T> adapter;
    @Getter private Class<? extends BaseStormModel> model;
    @Getter private int max;
    @Getter private KeyType keyType;
    @Getter private boolean unique;
    @Getter private boolean autoIncrement;
    @Getter private boolean notNull;
    @Getter private boolean useBlob;
    @Getter private String defaultValue;
    @Getter private Field reflectedField;
    @Getter private Storm storm;

    public ParsedField(Storm storm, Class<? extends BaseStormModel> modelClass, Class<T> type, Field field) {
        this.storm = storm;
        this.model = modelClass;
        this.type = type;
        this.javaFieldName = field.getName();
        this.columnName = Reflection.getAnnotatedFieldName(field);
        this.max = Reflection.getAnnotatedFieldMax(field);
        this.keyType = Reflection.getAnnotatedKeyType(field);
        this.unique = Reflection.getAnnotatedUnique(field);
        this.autoIncrement = Reflection.getAnnotatedAutoIncrement(field);
        this.notNull = Reflection.getAnnotatedNotNull(field);
        this.defaultValue = Reflection.getAnnotatedDefaultValue(field);
        this.useBlob = Reflection.getAnnotatedUseBlob(field);
        this.reflectedField = field;
        this.adapter = TypeRegistry.getAdapterFor(this);
    }

    @SneakyThrows
    public Object valueOn(BaseStormModel model) {
        this.reflectedField.setAccessible(true);
        return this.adapter.toSql(
                this.storm, (T) this.reflectedField.get(model)
        );
    }

    public Object toSqlStringType(Object toEscape) {
        return (adapter.escapeAsString() ? "'" + toEscape + "'" : toEscape);
    }

}
