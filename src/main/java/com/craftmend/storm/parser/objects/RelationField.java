package com.craftmend.storm.parser.objects;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.BaseStormModel;
import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;

import java.lang.reflect.Field;

public class RelationField<T> {

    @Getter
    private Class type;
    @Getter private String javaFieldName;
    @Getter private String columnName;
    @Getter private Class<? extends BaseStormModel> model;
    @Getter private Field reflectedField;
    @Getter private Storm storm;

    @Getter private String matchToField;

    public RelationField(Storm storm, Class<? extends BaseStormModel> modelClass, Class<T> type, Field field) {
        this.storm = storm;
        this.model = modelClass;
        this.type = type;
        this.javaFieldName = field.getName();
        this.columnName = Reflection.getAnnotatedFieldName(field);
        this.matchToField = Reflection.getAnnotatedMatchTo(field);
        this.reflectedField = field;
    }

    public ModelParser getTargetParser() {
        return storm.getParsedModel(getMatchToType(), false);
    }

    public Class<? extends BaseStormModel> getMatchToType() {
        return Reflection.getAnnotatedReference(storm, reflectedField).getOwnType();
    }

}
