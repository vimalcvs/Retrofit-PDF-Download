package com.vimalcvs.upgkhindi.utils;

import android.app.Application;

import com.vimalcvs.upgkhindi.rests.ApplicationLifecycleManager;

public class MyApplication extends Application {

    private static MyApplication singleton = null;

    public static MyApplication getInstance() {
        if (singleton == null) {
            singleton = new MyApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ApplicationLifecycleManager());
        singleton = this;

    }

}