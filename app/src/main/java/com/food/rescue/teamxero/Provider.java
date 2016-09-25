package com.food.rescue.teamxero;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rishi on 9/24/16.
 */
public class Provider {
    private String name;
    private String lat;
    private String lon;
    private String mobile;
    private String address;
    private String type;


    public LatLng getPosition(){
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
