package com.craftmend.storm.api;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.builders.StatementBuilder;
import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.ModelParser;
import lombok.Getter;
import lombok.Setter;

/**
 * Extends BaseStormModel to have a primary key
 * Should be used in most situations
 */
public abstract class StormModel extends BaseStormModel {

    @Getter
    @Setter
    @Column(
            autoIncrement = true,
            keyType = KeyType.PRIMARY
    )
    public Integer id;

}
