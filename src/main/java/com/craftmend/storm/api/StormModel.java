package com.craftmend.storm.api;

import com.craftmend.storm.api.builders.StatementBuilder;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.ModelParser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class StormModel {

    @Getter private final ModelParser parsedSelf;
    @Getter private final StatementBuilder statementBuilder;

    @Column(
            unique = true,
            autoIncrement = true,
            notNull = true,
            keyType = KeyType.PRIMARY
    )
    private Integer id;

    public StormModel() {
        this.parsedSelf = new ModelParser(getClass());
        this.statementBuilder = new StatementBuilder(this);
    }

}
