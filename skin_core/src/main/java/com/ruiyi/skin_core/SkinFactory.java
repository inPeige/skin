package com.ruiyi.skin_core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by liupei on 2018/3/16.
 */

public class SkinFactory implements LayoutInflater.Factory2, Observer {
    // 属性处理类
    SkinAttribute skinAttribute;

    public SkinFactory() {
        skinAttribute = new SkinAttribute();
    }

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();
    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    public final String[] a = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createViewFormTag(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (view != null) {
            Log.e("SkinFactory", String.format("检查[%s]:" + name, context.getClass().getName()));
            skinAttribute.load(view, attrs);
        }
        return view;
    }


    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass
                        (name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    private View createViewFormTag(String name, Context context, AttributeSet attrs) {
        if (-1 != name.indexOf('.')) {//包含自定义控件
            return null;
        }
        View view = null;
        for (int i = 0; i < a.length; i++) {
            view = createView(a[i] + name, context, attrs);
            if (view != null) {
                break;
            }
        }
        return view;
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        // 更换皮肤
        skinAttribute.applySkin();
    }
}
