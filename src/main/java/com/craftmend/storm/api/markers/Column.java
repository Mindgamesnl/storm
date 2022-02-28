package com.craftmend.storm.api.markers;

import com.craftmend.storm.api.enums.KeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * @return Name of the database column. Leaving this empty will convert the java field name to snake_case
     */
    String name() default "";

    /**
     * @return The maximum length of the value, used in types like varchars
     */
    int length() default 255;

    /**
     * @return Define additional key types
     */
    KeyType keyType() default KeyType.NONE;

    /**
     * @return Configure an (optional) default value. Integers will be wrapped appropriately.
     */
    String defaultValue() default "";

    /**
     * @return Enable auto incrementing
     */
    boolean autoIncrement() default false;

    /**
     * @return Require this value to be unique during validation
     */
    boolean unique() default false;

    /**
     * @return Null is for bitches
     */
    boolean notNull() default false;

}
