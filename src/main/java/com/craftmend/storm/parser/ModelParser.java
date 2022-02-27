package com.craftmend.storm.parser;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.parser.objects.ModelField;
import com.craftmend.storm.utils.Reflection;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModelParser {

    @Getter
    private final String tableName;
    @Getter private final ModelField[] parsedFields;

    public ModelParser(Class<? extends StormModel> modelClazz) {
        this.tableName = Reflection.TableNameFromClass(modelClazz);

        // load fields
        List<ModelField> tempFields = new ArrayList<>();
        for (Field declaredField : Reflection.getAllFields(new ArrayList<>(), modelClazz)) {
            if (declaredField.isAnnotationPresent(Column.class)) {
                tempFields.add(new ModelField(modelClazz, declaredField));
            }
        }
        parsedFields = new ModelField[tempFields.size()];
        tempFields.toArray(parsedFields);
    }

}
