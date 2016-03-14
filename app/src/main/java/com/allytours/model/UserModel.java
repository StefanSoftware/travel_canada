package com.allytours.model;

/**
 * Created by Administrator on 2/16/2016.
 */
public class UserModel {
    String usertype;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    String fullname;
    String email;
    String password;
    String phoneNumber;
    String birthday;
    String gender;
    String country_code;
    String city_country;
    String address;
    String userPhotoURL;
    String licensePhotoURL;
    String cardNumber;
    String cvv;
    String ssn_sin;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    String offset;

    public String getSsn_sin() {
        return ssn_sin;
    }

    public void setSsn_sin(String ssn_sin) {
        this.ssn_sin = ssn_sin;
    }

    public String getExpireday_month() {
        return expireday_month;
    }

    public void setExpireday_month(String expireday_month) {
        this.expireday_month = expireday_month;
    }

    public String getExpireday_year() {
        return expireday_year;
    }

    public void setExpireday_year(String expireday_year) {
        this.expireday_year = expireday_year;
    }

    String expireday_month;
    String expireday_year;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCity_country() {
        return city_country;
    }

    public void setCity_country(String city_country) {
        this.city_country = city_country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPhotoURL() {
        return userPhotoURL;
    }

    public void setUserPhotoURL(String userPhotoURL) {
        this.userPhotoURL = userPhotoURL;
    }

    public String getLicensePhotoURL() {
        return licensePhotoURL;
    }

    public void setLicensePhotoURL(String licensePhotoURL) {
        this.licensePhotoURL = licensePhotoURL;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    //////////////////////bank info
    String bank_country, bank_currency, bank_rounting_number, bank_account_number, bank_account_holder_name, bank_account_holder_type;

    public String getBank_country() {
        return bank_country;
    }

    public void setBank_country(String bank_country) {
        this.bank_country = bank_country;
    }

    public String getBank_currency() {
        return bank_currency;
    }

    public void setBank_currency(String bank_currency) {
        this.bank_currency = bank_currency;
    }

    public String getBank_rounting_number() {
        return bank_rounting_number;
    }

    public void setBank_rounting_number(String bank_rounting_number) {
        this.bank_rounting_number = bank_rounting_number;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getBank_account_holder_name() {
        return bank_account_holder_name;
    }

    public void setBank_account_holder_name(String bank_account_holder_name) {
        this.bank_account_holder_name = bank_account_holder_name;
    }

    public String getBank_account_holder_type() {
        return bank_account_holder_type;
    }

    public void setBank_account_holder_type(String bank_account_holder_type) {
        this.bank_account_holder_type = bank_account_holder_type;
    }


}
