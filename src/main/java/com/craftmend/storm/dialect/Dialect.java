package com.craftmend.storm.dialect;

import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.utils.ColumnDefinition;

public interface Dialect {

    ColumnDefinition compileColumn(ParsedField<?> modelField);

}
