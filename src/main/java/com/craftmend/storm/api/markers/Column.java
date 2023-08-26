package com.craftmend.storm.api.markers;

import com.craftmend.storm.api.BaseStormModel;
import com.craftmend.storm.api.enums.ColumnType;
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
     * @return True if the java type should be ignored and the value should be parsed as gson blob
     */
    boolean storeAsBlob() default false;

    /**
     * @return The type of this column. Value is a normal mapped value, but ONE_TO_MANY attempts to polyfill this value
     *      with all matching elements
     */
    ColumnType type() default ColumnType.VALUE;

    /**
     * @return The maximum length of the value, used in types like varchars
     */
    int length() default 255;

    /**
     * @return Define additional key types
     */
    KeyType keyType() default KeyType.NONE;

    /**
     * @return Where a key points to
     */
    Class<? extends BaseStormModel>[] references() default {};

    /**
     * @return Field in the @link{referenced} class to match while loading
     */
    String matchTo() default "";

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
