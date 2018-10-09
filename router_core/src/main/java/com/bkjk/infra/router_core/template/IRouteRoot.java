package com.bkjk.infra.router_core.template;

import java.util.Map;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public interface IRouteRoot {
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
