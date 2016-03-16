package com.allytours.controller;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.orm.SugarContext;


//import com.googlservices.pushnotifications.PlayServicesHelper;

public class AllytoursApplication extends Application {

    public static AllytoursApplication app;
    public static SharedPreferences appPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);
        app = this;
        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);




    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}