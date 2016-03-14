package com.allytours.controller.Helpers;

import android.content.Context;

import com.allytours.controller.Utilities.Utils;
import com.allytours.model.Constant;

/**
 * Created by Administrator on 2/29/2016.
 */
public class InitHelper {
    public static void initPreference(Context context) {
        Utils.setOnPreference(context, Constant.USER_TYPE, "");
        Utils.setOnPreference(context, Constant.FIRST_NAME, "");
        Utils.setOnPreference(context, Constant.LAST_NAME, "");
        Utils.setOnPreference(context, Constant.EMAIL, "");
        Utils.setOnPreference(context, Constant.PHONE_NUMBER, "");
        Utils.setOnPreference(context, Constant.USER_PHOTO, "");
        Utils.setOnPreference(context, Constant.BIRTHDAY, "");
        Utils.setOnPreference(context, Constant.GENDER, "");
        Utils.setOnPreference(context, Constant.SSN_SIN, "");
    }
}
