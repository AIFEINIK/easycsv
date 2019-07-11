package com.feinik.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Feinik
 * @Discription ${DISCRIPTION}
 * @Data 2019/7/10
 * @Version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CsvProperty {

    /**
     * 列名称
     * @return value
     */
    String value();


    /**
     * 列索引
     * @return index
     */
    int index() default 99999;


    /**
     * format value with MessageFormat.format
     * @return format
     */
    String format() default "{0}";
}
