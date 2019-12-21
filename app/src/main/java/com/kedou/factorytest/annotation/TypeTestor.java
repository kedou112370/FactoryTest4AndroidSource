package com.kedou.factorytest.annotation;

import java.lang.annotation.Repeatable;

/**
 * @author kedou
 * @data 2019/12/17
 */
@Repeatable(TypesTestor.class)
public @interface TypeTestor {
    String type() default "";
}
