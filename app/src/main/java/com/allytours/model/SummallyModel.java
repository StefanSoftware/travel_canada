package com.allytours.model;

/**
 * Created by Administrator on 2/9/2016.
 */
public class SummallyModel {
    String location_id;
    String totop;
    String tourcnt;
    String tottours;
    String tottoursamt;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String locaion_id) {
        this.location_id = locaion_id;
    }

    public String getTotop() {
        return totop;
    }

    public void setTotop(String totop) {
        this.totop = totop;
    }

    public String getTourcnt() {
        return tourcnt;
    }

    public void setTourcnt(String tourcnt) {
        this.tourcnt = tourcnt;
    }

    public String getTottours() {
        return tottours;
    }

    public void setTottours(String tottours) {
        this.tottours = tottours;
    }

    public String getTottoursamt() {
        return tottoursamt;
    }

    public void setTottoursamt(String tottoursamt) {
        this.tottoursamt = tottoursamt;
    }

    public String getTotreviews() {
        return totreviews;
    }

    public void setTotreviews(String totreviews) {
        this.totreviews = totreviews;
    }

    public String getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(String avgrating) {
        this.avgrating = avgrating;
    }

    public String getUpdatedt() {
        return updatedt;
    }

    public void setUpdatedt(String updatedt) {
        this.updatedt = updatedt;
    }

    String totreviews;
    String avgrating;
    String updatedt;

    String city;
    String region;
    public String getCity() {
        return city;
    }

    public void setCity(String tours) {
        this.city = tours;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_group() {
        return region_group;
    }

    public void setRegion_group(String region_group) {
        this.region_group = region_group;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDefault_currency() {
        return default_currency;
    }

    public void setDefault_currency(String default_currency) {
        this.default_currency = default_currency;
    }

    String region_group;
    String country;
    String default_currency;
    String latitude, longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String tours_region_country;

    public String getTours_region_country() {
        return tours_region_country;
    }

    public void setTours_region_country(String tours_region_country) {
        this.tours_region_country = tours_region_country;
    }

}
