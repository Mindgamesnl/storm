package com.craftmend.storm.api;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.builders.StatementBuilder;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.ModelParser;
import lombok.Getter;
import lombok.Setter;

/**
 * Core of any storm model, has required api parsing flags and configures a default ID primary key
 */
public abstract class StormModel {

    private ModelParser<? extends StormModel> parsedSelf;
    private StatementBuilder statementBuilder;

    @Getter
    @Setter
    @Column(
            autoIncrement = true,
            keyType = KeyType.PRIMARY
    )
    private Integer id;

    public ModelParser<? extends StormModel> parsed(Storm orm) {
        if (parsedSelf != null) return parsedSelf;
        parsedSelf = new ModelParser(getClass(), orm, this);
        return parsedSelf;
    }

    public StatementBuilder statements() {
        if (statementBuilder != null) return statementBuilder;
        statementBuilder = new StatementBuilder(this);
        return statementBuilder;
    }

    public void preSave() {}
    public void preDelete() {}
    public void postSave() {}
    public void postDelete() {}

}
