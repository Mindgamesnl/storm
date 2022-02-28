package com.craftmend.storm.dialect;

import com.craftmend.storm.parser.objects.ModelField;

public interface Dialect {

    String compileColumn(ModelField<?> modelField);

}
