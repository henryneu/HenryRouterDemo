package com.neu.henry.base_module;

import android.app.Application;
import android.content.Context;

/**
 * Author: zhouzhenhua
 * Date: 2018/12/17
 * Version: 1.0.0
 * Description:
 */
public class HenryApplication extends Application {

    private static HenryApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initRouter(this);
    }

    public static Context getInstance() {
        return sInstance;
    }

    private void initRouter(HenryApplication henryApplication) {
    }
}
