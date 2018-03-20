package com.ruiyi.skin_core.uitls;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.ruiyi.skin_core.SkinManager;

/**
 * Created by liupei on 2018/3/16.
 */

public class SkinResourcess {
    private Resources skinResources;
    private Resources appResources;
    private static SkinResourcess skinManager;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;

    private SkinResourcess(Context context) {
        this.appResources = context.getResources();
    }

    public static void init(Context context) {
        synchronized (SkinManager.class) {
            if (skinManager == null) {
                skinManager = new SkinResourcess(context);
            }
        }

    }

    public static SkinResourcess getInstance() {
        return skinManager;
    }

    public void applySkin(Resources resources, String pkgName) {
        skinResources = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        //在皮肤包中不一定就是 当前程序的 id
        //获取对应id 在当前的名称 colorPrimary
        //R.drawable.ic_launcher
        String resName = appResources.getResourceEntryName(resId);//ic_launcher
        String resType = appResources.getResourceTypeName(resId);//drawable
        int skinId = skinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    public void reset() {
        skinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return appResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getColor(resId);
        }
        return skinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return appResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getColorStateList(resId);
        }
        return skinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        //如果有皮肤  isDefaultSkin false 没有就是true
        if (isDefaultSkin) {
            return appResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getDrawable(resId);
        }
        return skinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = appResources.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return appResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return appResources.getString(skinId);
            }
            return skinResources.getString(skinId);
        } catch (Resources.NotFoundException e) {

        }
        return null;
    }

    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if (isDefaultSkin) {
                typeface = Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath);
                return typeface;

            }
            typeface = Typeface.createFromAsset(skinResources.getAssets(), skinTypefacePath);
            return typeface;
        } catch (RuntimeException e) {
        }
        return Typeface.DEFAULT;
    }
}
