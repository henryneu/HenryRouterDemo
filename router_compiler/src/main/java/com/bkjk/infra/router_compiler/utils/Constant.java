package com.bkjk.infra.router_compiler.utils;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public class Constant {
    public static final String ACTIVITY = "android.app.Activity";
    public static final String ISERVICE = "com.bkjk.infra.router_core.template.IService";

    public static final String ARGUMENTS_NAME = "moduleName";
    public static final String ANNOTATION_TYPE_ROUTE = "com.bkjk.infra.router_annotation.Route";

    public static final String METHOD_LOAD_INTO = "loadInto";

    public static final String IROUTE_ROOT = "com.bkjk.infra.router_core.template.IRouteRoot";
    public static final String IROUTE_GROUP = "com.bkjk.infra.router_core.template.IRouteGroup";

    public static final String SEPARATOR = "_";
    public static final String PROJECT = "EaseRouter";
    public static final String NAME_OF_ROOT = PROJECT + SEPARATOR + "Root" + SEPARATOR;
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
    /**
     * 指定生成的类文件的目录
     */
    public static final String PACKAGE_OF_GENERATE_FILE = "com.bkjk.infra.router";
}
