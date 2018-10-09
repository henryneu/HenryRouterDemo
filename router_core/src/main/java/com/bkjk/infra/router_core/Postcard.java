package com.bkjk.infra.router_core;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;

import com.bkjk.infra.router_annotation.modle.RouteMeta;
import com.bkjk.infra.router_core.callback.NavigationCallback;
import com.bkjk.infra.router_core.template.IService;

import java.util.ArrayList;

/**
 * Author: zhouzhenhua
 * Date: 2018/10/9
 * Version: 1.0.0
 * Description:
 */
public class Postcard extends RouteMeta {

    private Bundle mBundle;

    private int mFlags = -1;

    // 服务
    private IService mIService;
    // 新版风格
    private Bundle mOptionsCompat;
    // 老版风格
    private int mEnterAnim;
    private int mExitAnim;

    public Postcard(String path, String group) {
        this(path, group, null);
    }

    public Postcard(String path, String group, Bundle bundle) {
        setPath(path);
        setGroup(group);
        this.mBundle = (null == bundle ? new Bundle() : bundle);
    }

    public void setService(IService iService) {
        mIService = iService;
    }

    public IService getService() {
        return mIService;
    }

    public Bundle getExtras() {
        return mBundle;
    }

    /**
     * Intent.FLAG_ACTIVITY_*_*
     * @param flag
     * @return
     */
    public Postcard withFlags(int flag) {
        this.mFlags = flag;
        return this;
    }

    public int getFlags() {
        return mFlags;
    }

    public Bundle getOptionsBundle() {
        return mOptionsCompat;
    }

    /**
     * 跳转动画
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public Postcard withTransition(int enterAnim, int exitAnim) {
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
        return this;
    }

    /**
     * 转场动画
     * @param compat
     * @return
     */
    public Postcard withOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat) {
            mOptionsCompat = compat.toBundle();
        }
        return this;
    }

    public int getEnterAnim() {
        return mEnterAnim;
    }

    public int getExitAnim() {
        return mExitAnim;
    }

    public Postcard withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }


    public Postcard withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }


    public Postcard withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }


    public Postcard withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }


    public Postcard withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }


    public Postcard withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }


    public Postcard withByte(@Nullable String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }


    public Postcard withChar(@Nullable String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }


    public Postcard withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }


    public Postcard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }


    public Postcard withStringArray(@Nullable String key, @Nullable String[] value) {
        mBundle.putStringArray(key, value);
        return this;
    }


    public Postcard withBooleanArray(@Nullable String key, boolean[] value) {
        mBundle.putBooleanArray(key, value);
        return this;
    }


    public Postcard withShortArray(@Nullable String key, short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }


    public Postcard withIntArray(@Nullable String key, int[] value) {
        mBundle.putIntArray(key, value);
        return this;
    }


    public Postcard withLongArray(@Nullable String key, long[] value) {
        mBundle.putLongArray(key, value);
        return this;
    }


    public Postcard withDoubleArray(@Nullable String key, double[] value) {
        mBundle.putDoubleArray(key, value);
        return this;
    }


    public Postcard withByteArray(@Nullable String key, byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }


    public Postcard withCharArray(@Nullable String key, char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }


    public Postcard withFloatArray(@Nullable String key, float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }


    public Postcard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public Postcard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends
            Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public Postcard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public Postcard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Object navigation() {
        return HenryRouter.getInstance().navigation(null, this, -1, null);
    }

    public Object navigation(Context context) {
        return HenryRouter.getInstance().navigation(context, this, -1, null);
    }


    public Object navigation(Context context, NavigationCallback callback) {
        return HenryRouter.getInstance().navigation(context, this, -1, callback);
    }

    public Object navigation(Context context, int requestCode) {
        return HenryRouter.getInstance().navigation(context, this, requestCode, null);
    }

    public Object navigation(Context context, int requestCode, NavigationCallback callback) {
        return HenryRouter.getInstance().navigation(context, this, requestCode, callback);
    }
}