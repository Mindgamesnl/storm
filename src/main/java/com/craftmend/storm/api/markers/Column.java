package com.craftmend.storm.api.markers;

import com.craftmend.storm.api.enums.KeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";
    int length() default 255;
    KeyType keyType() default KeyType.NONE;
    String defaultValue() default "";
    boolean autoIncrement() default false;
    boolean unique() default false;
    boolean notNull() default false;

}
