package com.ruiyi.skin_core;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiyi.skin_core.uitls.SkinResourcess;
import com.ruiyi.skin_core.uitls.SkinThemeUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liupei on 2018/3/16.
 */

public class SkinAttribute {
    public static final List<String> list = new ArrayList<>();

    static {
        list.add("background");
        list.add("src");


        list.add("textColor");
        list.add("drawableLeft");
        list.add("drawableTop");
        list.add("drawableRight");
        list.add("drawableBottom");
    }

    private ArrayList<SkinView> skinViews = new ArrayList<SkinView>();

    public void load(View view, AttributeSet attrs) {
        ArrayList<SkinAttrParms> skinAttrParms = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if (list.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int id;
                if (attributeValue.startsWith("?")) {
                    int attrid = Integer.parseInt(attributeValue.substring(1));
                    id = SkinThemeUitls.getThemeResid(view.getContext(), new int[]{attrid})[0];
                } else {
                    id = Integer.parseInt(attributeValue.substring(1));
                }
                if (id != 0) {
                    SkinAttrParms attrParms = new SkinAttrParms(attributeName, id);
                    skinAttrParms.add(attrParms);
                }
            }
        }
        //将View与之对应的可以动态替换的属性集合 放入 集合中
        if (!skinAttrParms.isEmpty()) {
            SkinView skinView = new SkinView(view, skinAttrParms);
            skinView.applySkin();
            skinViews.add(skinView);
        }
    }

    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin();
        }
    }

    public class SkinAttrParms {
        private String attrName;
        private int id;

        public SkinAttrParms(String attrName, int id) {
            this.attrName = attrName;
            this.id = id;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    class SkinView {
        View view;
        List<SkinAttrParms> parms;

        public SkinView(View view, List<SkinAttrParms> parms) {
            this.view = view;
            this.parms = parms;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void applySkin() {
            for (SkinAttrParms parms : parms) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (parms.attrName) {
                    case "background":
                        Object background = SkinResourcess.getInstance().getBackground(parms
                                .id);
                        //Color
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResourcess.getInstance().getBackground(parms
                                .id);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResourcess.getInstance().getColorStateList
                                (parms.id));
                        break;
                    case "drawableLeft":
                        left = SkinResourcess.getInstance().getDrawable(parms.id);
                        break;
                    case "drawableTop":
                        top = SkinResourcess.getInstance().getDrawable(parms.id);
                        break;
                    case "drawableRight":
                        right = SkinResourcess.getInstance().getDrawable(parms.id);
                        break;
                    case "drawableBottom":
                        bottom = SkinResourcess.getInstance().getDrawable(parms.id);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }
    }
}
