package com.bkjk.infra.router_compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public class Utils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
