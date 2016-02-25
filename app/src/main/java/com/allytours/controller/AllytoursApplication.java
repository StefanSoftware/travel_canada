package com.allytours.controller;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;


//import com.googlservices.pushnotifications.PlayServicesHelper;

public class AllytoursApplication extends Application {

    public static AllytoursApplication app;
//    public static QBManager qbManager;
    public static SharedPreferences appPrefs;
//    public static HttpCommunicator communicator;
//    public static RequestBuilder requestBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);

//        communicator = new HttpCommunicator(Constant.SEVER_URL);
//        requestBuilder = new RequestBuilder();
    }

//    public static void registerDeviceToken(Activity activity) {
//        PlayServicesHelper playServicesHelper = new PlayServicesHelper(activity);
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}