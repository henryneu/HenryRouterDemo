package com.bkjk.infra.router_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Route {
    /**
     * 路由的地址
     * @return
     */
    String path();

    /**
     * 将路由节点进行分组，可以实现动态加载
     * @return
     */
    String group() default "";
}
