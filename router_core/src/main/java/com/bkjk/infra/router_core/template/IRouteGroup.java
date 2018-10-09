package com.bkjk.infra.router_core.template;

import com.bkjk.infra.router_annotation.modle.RouteMeta;

import java.util.Map;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
