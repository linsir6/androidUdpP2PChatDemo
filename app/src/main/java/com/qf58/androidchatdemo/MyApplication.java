package com.qf58.androidchatdemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePalApplication;

/**
 * Created by linSir
 * date at 2017/12/17.
 * describe:
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        LitePalApplication.initialize(this);
    }
}
