package com.ruiyi.skin_core;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.ruiyi.skin_core.uitls.SkinActivityLifecycle;
import com.ruiyi.skin_core.uitls.SkinPreference;
import com.ruiyi.skin_core.uitls.SkinResourcess;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by liupei on 2018/3/16.
 */

public class SkinManager extends Observable {

    private static SkinManager skinManager;
    private Application application;

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResourcess.init(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (skinManager == null) {
                skinManager = new SkinManager(application);
            }
        }

    }

    public static SkinManager getInstance() {
        return skinManager;
    }

    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance().setSkin("");
            SkinResourcess.getInstance().reset();
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
                method.setAccessible(true);
                method.invoke(assetManager, path);
                Resources resources = application.getResources();
                Resources skinRes = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

                //获取外部Apk(皮肤包) 包名
                PackageManager mPm = application.getPackageManager();
                PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager
                        .GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResourcess.getInstance().applySkin(skinRes, packageName);
                //记录
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers();
    }
}
