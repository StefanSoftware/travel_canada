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
    ArrayList<String> arrImage = new ArrayList<>();
    String title;
    String price;
    String tour_id;
    String cityName;
    String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTour_id() {
        return tour_id;
    }

    public void setTour_id(String tour_id) {
        this.tour_id = tour_id;
    }

    String currency_unit;
    String review_count;
    String rate;
    String duration;
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

    String created_date, total_reviews, average_rating;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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

    public ArrayList<String> getArrPicturePath() {
        return arrPicturePath;
    }

    public void setArrPicturePath(ArrayList<String> arrPicturePath) {
        this.arrPicturePath = arrPicturePath;
    }

    String specifiedCityNames;

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

    public String getSpecifiedCityNames() {
        return specifiedCityNames;
    }

    public void setSpecifiedCityNames(String specifiedCityNames) {
        this.specifiedCityNames = specifiedCityNames;
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

    public String getDurationUnit() {
        return durationDay;
    }

    public void setDurationUnit(String durationDay) {
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

    public ArrayList<String> getArrImage() {
        return arrImage;
    }

    public void setArrImage(ArrayList<String> arrImage) {
        this.arrImage = arrImage;
    }
}
