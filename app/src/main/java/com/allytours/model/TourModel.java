package com.allytours.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2/9/2016.
 */
public class TourModel  implements Serializable{
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    ArrayList<Integer> arrImage = new ArrayList<>();
    String title;
    String price;

    String currency_unit;
    String review_count;
    String rate;
    String duration;
    String start_time;
    String end_time;
    String days;
    String adultCount ;
    String childCount;
    String totalPrice;
    ArrayList<String> arrCountry;
    String  lastMessageCount;
    String lastMessage;
    LocationModel locationModel;
    String lastMessageTime;
    boolean is_romantic ;
    boolean is_sightseeing;
    boolean is_adventure;
    String is_private;

    String adultPrice, childPrice;
    String locationIds, roundTripLocatioIds;
    String pictureCount;
    ArrayList<String> arrPicturePath;
    String active;
    String tourType;
    String languages;
    String attractions;
    String inclusions;

    public ArrayList<String> getArrPicturePath() {
        return arrPicturePath;
    }

    public void setArrPicturePath(ArrayList<String> arrPicturePath) {
        this.arrPicturePath = arrPicturePath;
    }

    String specifiedCityIds;

    public String getInclusionOthers() {
        return inclusionOthers;
    }

    public void setInclusionOthers(String inclusionOthers) {
        this.inclusionOthers = inclusionOthers;
    }

    String inclusionOthers;
    String frequency, startDate, startDay, startTime;
    String durationDay, durationTime;

    public String getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(String adultPrice) {
        this.adultPrice = adultPrice;
    }

    public String getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(String childPrice) {
        this.childPrice = childPrice;
    }

    public String getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(String locationIds) {
        this.locationIds = locationIds;
    }

    public String getRoundTripLocatioIds() {
        return roundTripLocatioIds;
    }

    public void setRoundTripLocatioIds(String roundTripLocatioIds) {
        this.roundTripLocatioIds = roundTripLocatioIds;
    }

    public String getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(String pictureCount) {
        this.pictureCount = pictureCount;
    }



    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getSpecifiedCityIds() {
        return specifiedCityIds;
    }

    public void setSpecifiedCityIds(String specifiedCityIds) {
        this.specifiedCityIds = specifiedCityIds;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(String durationDay) {
        this.durationDay = durationDay;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public TourModel() {

    }

    public String getLastMessageCount() {
        return lastMessageCount;
    }

    public void setLastMessageCount(String lastMessageCount) {
        this.lastMessageCount = lastMessageCount;
    }


    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public String getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(String adultCount) {
        this.adultCount = adultCount;
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency_unit() {
        return currency_unit;
    }

    public void setCurrency_unit(String currency_unit) {
        this.currency_unit = currency_unit;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public ArrayList<String> getArrCountry() {
        return arrCountry;
    }

    public void setArrCountry(ArrayList<String> arrCountry) {
        this.arrCountry = arrCountry;
    }

    public boolean is_romantic() {
        return is_romantic;
    }

    public void setIs_romantic(boolean is_romantic) {
        this.is_romantic = is_romantic;
    }

    public boolean is_sightseeing() {
        return is_sightseeing;
    }

    public void setIs_sightseeing(boolean is_sightseeing) {
        this.is_sightseeing = is_sightseeing;
    }

    public boolean is_adventure() {
        return is_adventure;
    }

    public void setIs_adventure(boolean is_adventure) {
        this.is_adventure = is_adventure;
    }

    public String getIs_private() {
        return is_private;
    }

    public void setIs_private(String is_private) {
        this.is_private = is_private;
    }

    public ArrayList<Integer> getArrImage() {
        return arrImage;
    }

    public void setArrImage(ArrayList<Integer> arrImage) {
        this.arrImage = arrImage;
    }
}
