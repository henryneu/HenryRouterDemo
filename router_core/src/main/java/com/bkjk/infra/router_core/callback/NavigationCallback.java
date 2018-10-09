package com.bkjk.infra.router_core.callback;

import com.bkjk.infra.router_core.Postcard;

/**
 * Author: zhouzhenhua
 * Date: 2018/10/9
 * Version: 1.0.0
 * Description:
 */
public interface NavigationCallback {
    /**
     * 找到跳转页面
     * @param postcard
     */
    void onFound(Postcard postcard);

    /**
     * 未找到
     * @param postcard
     */
    void onLost(Postcard postcard);

    /**
     * 成功跳转
     * @param postcard
     */
    void onArrival(Postcard postcard);
}
