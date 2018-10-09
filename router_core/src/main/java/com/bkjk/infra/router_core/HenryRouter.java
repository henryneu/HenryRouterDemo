package com.bkjk.infra.router_core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bkjk.infra.router_annotation.modle.RouteMeta;
import com.bkjk.infra.router_core.callback.NavigationCallback;
import com.bkjk.infra.router_core.exception.NoRouteFoundException;
import com.bkjk.infra.router_core.template.IRouteGroup;
import com.bkjk.infra.router_core.template.IRouteRoot;
import com.bkjk.infra.router_core.template.IService;
import com.bkjk.infra.router_core.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/28
 * Version: 1.0.0
 * Description:
 */
public class HenryRouter {

    private static final String TAG = HenryRouter.class.getSimpleName();
    private static final String ROUTE_ROOT_PAKCAGE = "com.bkjk.infra.henryrouter.routes";
    private static final String SDK_NAME = "HenryRouter";
    private static final String SEPARATOR = "_";
    private static final String SUFFIX_ROOT = "Root";

    private static HenryRouter sInstance;
    private Handler mHandler;
    private static Application mContext;

    private HenryRouter() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式获取 HenryRouter
     * @return
     */
    public static HenryRouter getInstance() {
        synchronized (HenryRouter.class) {
            if (sInstance == null) {
                sInstance = new HenryRouter();
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        mContext = application;
        try {
            loadInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败!", e);
        }
    }

    /**
     * 获取加载路由分组表
     */
    private static void loadInfo() throws PackageManager.NameNotFoundException, InterruptedException,
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 获得所有 apt生成的路由类的全类名 (路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);
        for (String className : routerMap) {
            if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                // Root 中注册的是分组信息,将分组信息加入仓库
                ((IRouteRoot) Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.groupsIndex);
            }
        }
        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : Warehouse.groupsIndex.entrySet()) {
            Log.d(TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry.getValue() + "]");
        }
    }

    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return build(path, extractGroup(path));
        }
    }

    public Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效!");
        } else {
            return new Postcard(path, group);
        }
    }

    /**
     * 提取 path 的组别
     * @param path
     * @return
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new RuntimeException(path + " : 不能提取group!");
        }
        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : 不能提取group!");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Object navigation(Context context, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        try {
            prepareCard(postcard);
        } catch (NoRouteFoundException e) {
            e.printStackTrace();
            if (null != callback) {
                callback.onLost(postcard);
            }
        }
        if (null != callback) {
            callback.onFound(postcard);
        }

        switch (postcard.getType()) {
            case ACTIVITY:
                final Context currentContext = (null == context ? mContext : context);
                final Intent intent = new Intent(currentContext, postcard.getDestination());
                intent.putExtras(postcard.getExtras());
                int flags = postcard.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if(!(currentContext instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (requestCode > 0) {
                            ActivityCompat.startActivityForResult((Activity) currentContext, intent, requestCode, postcard.getOptionsBundle());
                        } else {
                            ActivityCompat.startActivity(currentContext, intent, postcard.getOptionsBundle());
                        }

                        if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim()) && currentContext instanceof Activity) {
                            // 老版本
                            ((Activity) currentContext).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }
                        // 跳转完成
                        if (null != callback) {
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            case ISERVICE:
                return postcard.getService();
            default:
                break;
        }
        return null;
    }

    /**
     * 准备跳卡
     * @param postcard
     */
    private void prepareCard(Postcard postcard) {
        RouteMeta routeMeta = Warehouse.routes.get(postcard.getPath());
        if (null == routeMeta) {
            Class<? extends IRouteGroup> groupMeta = Warehouse.groupsIndex.get(postcard.getGroup());
            if (null == groupMeta) {
                throw new NoRouteFoundException("没找到对应路由：分组=" + postcard.getGroup() + "   路径=" + postcard.getPath());
            }
            IRouteGroup iGroupInstance;
            try {
                iGroupInstance = groupMeta.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("路由分组映射表记录失败.", e);
            }
            iGroupInstance.loadInto(Warehouse.routes);
            // 移除已遍历的分组,已经准备过就移除
            Warehouse.groupsIndex.remove(postcard.getGroup());
            // 再次进入遍历
            prepareCard(postcard);
        } else {
            postcard.setDestination(routeMeta.getDestination());
            postcard.setType(routeMeta.getType());
            switch (routeMeta.getType()) {
                case ISERVICE:
                    Class<?> destination = routeMeta.getDestination();
                    IService iService = Warehouse.services.get(destination);
                    if (null == iService) {
                        try {
                            iService = (IService) destination.getConstructor().newInstance();
                            Warehouse.services.put(destination, iService);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    postcard.setService(iService);
                    break;
                default:
                    break;
            }
        }
    }
}