package com.bkjk.infra.router_compiler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: zhouzhenhua
 * Date: 2018/12/17
 * Version: 1.0.0
 * Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface HenryAnnotation {
    String name() default "undefined";

    String text() default "";
}
