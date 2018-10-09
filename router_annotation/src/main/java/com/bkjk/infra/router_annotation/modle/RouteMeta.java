package com.bkjk.infra.router_annotation.modle;

import com.bkjk.infra.router_annotation.Route;

import javax.lang.model.element.Element;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public class RouteMeta {
    public enum Type {
        ACTIVITY, ISERVICE
    }

    private Type mType;

    /**
     * 节点（Activity）
     */
    private Element mElement;

    /**
     * 注解使用的类对象
     */
    private Class<?> mDestination;

    /**
     * 路由地址
     */
    private String mPath;

    /**
     * 路由分组
     */
    private String mGroup;

    public RouteMeta() {
    }

    public RouteMeta(Type type, Route route, Element element) {
        this(type, element, null, route.path(), route.group());
    }

    public RouteMeta(Type type, Element element, Class<?> destination, String path, String group) {
        this.mType = type;
        this.mDestination = destination;
        this.mElement = element;
        this.mPath = path;
        this.mGroup = group;
    }

    public static RouteMeta build(Type type, Class<?> destination, String path, String group) {
        return new RouteMeta(type, null, destination, path, group);
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public Element getElement() {
        return mElement;
    }

    public void setElement(Element element) {
        mElement = element;
    }

    public Class<?> getDestination() {
        return mDestination;
    }

    public void setDestination(Class<?> destination) {
        mDestination = destination;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }
}
