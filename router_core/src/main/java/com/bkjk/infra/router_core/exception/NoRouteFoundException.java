package com.bkjk.infra.router_core.exception;

/**
 * Author: zhouzhenhua
 * Date: 2018/10/9
 * Version: 1.0.0
 * Description:
 */
public class NoRouteFoundException extends RuntimeException {

    public NoRouteFoundException(String detailMessage) {
        super(detailMessage);
    }
}
