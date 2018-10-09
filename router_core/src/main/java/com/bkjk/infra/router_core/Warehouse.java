package com.bkjk.infra.router_core;

import com.bkjk.infra.router_annotation.modle.RouteMeta;
import com.bkjk.infra.router_core.template.IRouteGroup;
import com.bkjk.infra.router_core.template.IService;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/29
 * Version: 1.0.0
 * Description:存放路由映射关系
 */
public class Warehouse {
    /**
     * root 映射表 保存分组信息
     */
    static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();

    /**
     * group 映射表 保存组中的所有数据
     */
    static Map<String, RouteMeta> routes = new HashMap<>();

    /**
     * group 映射表 保存组中的所有数据
     */
    static Map<Class, IService> services = new HashMap<>();
    // TestServiceImpl.class , TestServiceImpl 没有再反射
}
