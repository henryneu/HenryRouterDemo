package com.bkjk.infra.router_compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public class LogUtils {
    private Messager mMessager;

    private LogUtils(Messager message) {
        mMessager = message;
    }

    public static LogUtils getInstance(Messager messager) {
        return new LogUtils(messager);
    }

    public void i(String message) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, message);
    }
}
