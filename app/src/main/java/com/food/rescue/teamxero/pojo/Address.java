package com.food.rescue.teamxero.pojo;

/**
 * Created by pratiksanglikar on 9/24/16.
 */

public class Address {

    private String address;
    private String city;
    private String State;
    private String zipcode;

    public Address(String address, String city, String state, String zipcode) {
        this.address = address;
        this.city = city;
        State = state;
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
