package com.craftmend.storm.api.enums;

import lombok.Getter;

public enum Where {

    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    EQUAL("="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),
    NOT_EQUAL("<>");

    @Getter private String sqlOp;
    Where(String s) {
        this.sqlOp = s;
    }
}
