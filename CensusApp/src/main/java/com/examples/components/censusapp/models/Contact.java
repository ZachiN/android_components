package com.examples.components.censusapp.models;

import android.util.Log;

/**
 * Created by Zachi on 05/09/2015.
 */
public class Contact {
    private String name;
    private String phoneNumber;
    private String streetAddress;
    private String city;

    public Contact() {
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        Log.e("CENSUS", "ADDRESS CHANGED TO " + streetAddress);
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        Log.e("CENSUS", "CITY CHANGED TO " + city);
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Log.e("CENSUS", "NAME CHANGED TO " + name);
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        Log.e("CENSUS", "PHONE NUMBER CHANGED TO " + phoneNumber);
        this.phoneNumber = phoneNumber;
    }
}
