package com.craftmend.storm.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ColumnDefinition {

    private String columnSql;
    private String configurationSql;

}
