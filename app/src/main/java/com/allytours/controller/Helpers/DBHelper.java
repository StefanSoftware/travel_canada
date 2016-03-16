package com.allytours.controller.Helpers;

import com.allytours.model.LocationModel;
import com.android.volley.request.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/2/2016.
 */
public class DBHelper {
    public static void insertLocation(LocationModel locationModel) {

        locationModel.save();
    }
    public static void insertLocationArray(List<LocationModel> locationModels) {
        for (int i = 0; i < locationModels.size(); i ++) {
            LocationModel locationModel = locationModels.get(i);
            locationModel.save();
        }
    }
    public static List<LocationModel> getAllLocation() {
        List<LocationModel> locationModels = LocationModel.listAll(LocationModel.class);
        return locationModels;
    }
    public static void update() {

    }
    public static void deleteAllLocation() {
        List<LocationModel> locationModels = getAllLocation();
        for (int i = 0; i < locationModels.size(); i ++) {
            LocationModel locationModel = locationModels.get(i);
            locationModel.delete();
        }
    }
    public static ArrayList<String> getAllCityName() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<LocationModel> arrLocations = getAllLocation();
        for(int i = 0; i < arrLocations.size(); i ++) {
            arrayList.add(arrLocations.get(i).getCity());
        }
        return arrayList;
    }

}
