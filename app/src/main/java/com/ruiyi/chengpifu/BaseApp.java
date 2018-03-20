package com.ruiyi.chengpifu;

import android.app.Application;

import com.ruiyi.skin_core.SkinManager;

/**
 * Created by liupei on 2018/3/18.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
