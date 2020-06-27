package com.longrise.ticketunion.ui.activity;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context mBaseContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseContext = getBaseContext();
    }

    public static Context getAppContext() {
        return mBaseContext;
    }

}
