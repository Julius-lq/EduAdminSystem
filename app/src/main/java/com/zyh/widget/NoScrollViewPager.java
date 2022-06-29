package com.zyh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll = false;

    public NoScrollViewPager(@NonNull Context paramContext) {
        this(paramContext, null);
    }

    public NoScrollViewPager(@NonNull Context paramContext, @Nullable AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool = this.isScroll;
        return !bool ? bool : super.onInterceptTouchEvent(paramMotionEvent);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool = this.isScroll;
        return !bool ? bool : super.onTouchEvent(paramMotionEvent);
    }

    public void setScrollable(boolean paramBoolean) {
        this.isScroll = paramBoolean;
    }
}
