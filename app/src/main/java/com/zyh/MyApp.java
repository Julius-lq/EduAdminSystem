package com.zyh;

import android.app.Application;

import com.xuexiang.xui.XUI;
import com.zyh.utills.CrashHandler;


import org.litepal.LitePal;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        LitePal.initialize(this);
        XUI.init(this);
        XUI.debug(true);
        super.onCreate();
    }
}
