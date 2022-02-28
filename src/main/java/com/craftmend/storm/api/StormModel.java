package com.craftmend.storm.api;

import com.craftmend.storm.api.builders.StatementBuilder;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.ModelParser;
import lombok.Getter;
import lombok.Setter;

public abstract class StormModel {

    private ModelParser parsedSelf;
    private StatementBuilder statementBuilder;

    @Getter
    @Setter
    @Column(
            unique = true,
            autoIncrement = true,
            keyType = KeyType.PRIMARY
    )
    private Integer id;

    public ModelParser parsed() {
        if (parsedSelf != null) return parsedSelf;
        parsedSelf = new ModelParser(getClass());
        return parsedSelf;
    }

    public StatementBuilder statements() {
        if (statementBuilder != null) return statementBuilder;
        statementBuilder = new StatementBuilder(this);
        return statementBuilder;
    }

}
