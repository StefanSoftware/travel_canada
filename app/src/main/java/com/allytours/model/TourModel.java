package com.allytours.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2/9/2016.
 */
public class TourModel implements Serializable{
    ArrayList<Integer> arrImage = new ArrayList<>();
    String title, price, currency_unit, review_count, rate, duration, start_time,
            end_time, days;
    ArrayList<String> arrCountry;
    String lastMessage, lastMessageCount;

    public String getLastMessageCount() {
        return lastMessageCount;
    }

    public void setLastMessageCount(String lastMessageCount) {
        this.lastMessageCount = lastMessageCount;
    }

    String adultCount, childCount, totalPrice;

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

    String lastMessageTime;

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

    public boolean is_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    boolean is_romantic, is_sightseeing, is_adventure, is_private;

    public ArrayList<Integer> getArrImage() {
        return arrImage;
    }

    public void setArrImage(ArrayList<Integer> arrImage) {
        this.arrImage = arrImage;
    }
}
