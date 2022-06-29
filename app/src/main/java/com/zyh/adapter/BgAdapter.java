package com.zyh.adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zyh.fragment.Bg1Fragment;
import com.zyh.fragment.Bg2Fragment;

//这是加载填充viewpager的fragment的适配器
public class BgAdapter extends FragmentStatePagerAdapter {
    public Context context;

    public int tabCount;

    public BgAdapter(Context paramContext, FragmentManager paramFragmentManager, int paramInt) {
        super(paramFragmentManager);
        this.context = paramContext;
        this.tabCount = paramInt;
    }

    public int getCount() {
        return this.tabCount;
    }

    public Fragment getItem(int paramInt) {
        return (Fragment) ((paramInt == 0) ? new Bg1Fragment() : ((paramInt == 1) ? new Bg2Fragment() : null));
    }

    public CharSequence getPageTitle(int paramInt) {
        return (paramInt == 0) ? "推荐壁纸" : ((paramInt == 1) ? "色块" : null);
    }
}
