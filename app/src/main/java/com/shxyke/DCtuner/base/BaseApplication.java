package com.shxyke.DCtuner.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.utils.SPUtils;

public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SPUtils.getInstance(this, Contents.SP_FILE_NAME);

        int NIGHT_MODE = (boolean) SPUtils.getData(Contents.SP_NIGHT_MODE_KEY, false) ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(NIGHT_MODE);
    }

    public static Context getContext(){
        return context;
    }

}

