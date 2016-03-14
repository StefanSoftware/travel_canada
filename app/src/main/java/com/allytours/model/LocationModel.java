package com.allytours.model;


import com.orm.SugarRecord;

/**
 * Created by Administrator on 2/9/2016.
 */
public class LocationModel  extends SugarRecord {
    String location_id;
    String total_op;
    String tour_count;
    String total_tours;
    String total_tour_amount;
    String total_reviews;
    String average_rating;
    String updated_date;
    String city;
    String region;
    String region_group;
    String country;
    String default_currency;
    String tours_region_country;

    String longitude;
    String latitude;

    public LocationModel() {

    }
    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getTotal_op() {
        return total_op;
    }

    public void setTotal_op(String total_op) {
        this.total_op = total_op;
    }

    public String getTour_count() {
        return tour_count;
    }

    public void setTour_count(String tour_count) {
        this.tour_count = tour_count;
    }

    public String getTotal_tours() {
        return total_tours;
    }

    public void setTotal_tours(String total_tours) {
        this.total_tours = total_tours;
    }

    public String getTotal_tour_amount() {
        return total_tour_amount;
    }

    public void setTotal_tour_amount(String total_tour_amount) {
        this.total_tour_amount = total_tour_amount;
    }

    public String getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(String total_reviews) {
        this.total_reviews = total_reviews;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getTours_region_country() {
        return tours_region_country;
    }

    public void setTours_region_country(String tours_region_country) {
        this.tours_region_country = tours_region_country;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }





}
