package com.zyh.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColorUtil {
//    public static ColorUtil ColorList = new ColorUtil();
//
//    public static List<View> ViewList = new ArrayList<View>();
//
//    public static List<View> viewList = new ArrayList<View>();
//
//    public static List<Color> colorList = new ArrayList<Color>();
//
//    public SharedPreferences color;
//
//    public void color(View paramView) {
//        if (!((ArrayList) ViewList).contains(paramView))
//            ((ArrayList<View>) viewList).add(paramView);
//        viewList.add(paramView);
////        viewList(paramView);
//    }
//
//    public void colorList() {
//        int j;
//        byte b = 0;
//        int i = 0;
//        while (true) {
//            j = b;
//            if (i < ((ArrayList) ViewList).size()) {
//                viewList(((ArrayList<View>) viewList).get(i));
//                i++;
//                continue;
//            }
//            break;
//        }
//        while (j < ((ArrayList) ViewList).size()) {
//            ViewList(((ArrayList<View>) ViewList).get(j));
//            j++;
//        }
//        Iterator<Color> iterator = ((ArrayList) colorList).iterator();
//        while (iterator.hasNext())
//            ((Color) iterator.next()).apply();
//        OooO0o.colorList().ViewList(new o00O0O());
//        OooO0o.colorList().ViewList(new OooOo00());
//    }
//
//    public final void viewList(View paramView) {
//        if (paramView == null)
//            return;
//        try {
//            BottomNavigationView bottomNavigationView = null;
//            MaterialButton materialButton = null;
//            ColorStateList colorStateList = ColorStateList.valueOf(colorList(paramView.getContext()));
//            int i = colorList(paramView.getContext());
//            if (paramView instanceof BottomNavigationView) {
//                bottomNavigationView = (BottomNavigationView) paramView;
//                bottomNavigationView.setItemTextColor(colorStateList);
//                bottomNavigationView.setItemIconTintList(colorStateList);
//                return;
//            }
//            materialButton.setBackgroundTintList(colorStateList);
//            return;
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            return;
//        }
//    }
//
//    public final void ViewList(View paramView) {
//        if (paramView == null)
//            return;
////        colorList.addAll(paramView.getContext(), paramView, colorList(paramView.getContext()), 0, 0, 0, 40, 40);
//        o0000O0O.OooOO0(paramView.getContext(), paramView, colorList(paramView.getContext()), 0, 0, 0, 40, 40);
//    }
//
//    public String OooO0o(Context paramContext) {
//        if (this.color == null)
//            this.color = paramContext.getSharedPreferences("背景色", 0);
//        return this.color.getString("背景色", "#06BA9F");
//    }
//
//    public int colorList(Context paramContext) {
//        return Color.parseColor(OooO0o(paramContext));
//    }
//
//    public static interface color {
//        void apply();
//    }
}