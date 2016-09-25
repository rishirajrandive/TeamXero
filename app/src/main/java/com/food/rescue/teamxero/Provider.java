package com.food.rescue.teamxero;

import com.food.rescue.teamxero.pojo.Address;
import com.food.rescue.teamxero.pojo.SearchTerm;

/**
 * Created by rishi on 9/24/16.
 */
public class Provider {

    private String contact;
    private boolean available;
    private String foodType;
    private String quantity;
    private String description;
    private String imageLink;
    private double timestamp;
    private double expiryDate;
    private String firstName;
    private String lastName;
    private SearchTerm location;
    private Address address;

    public Provider(String contact, boolean available, String foodType, String quantity, String description, String imageLink, double timestamp, double expiryDate, String firstName, String lastName, SearchTerm location, Address address) {
        this.contact = contact;
        this.available = available;
        this.foodType = foodType;
        this.quantity = quantity;
        this.description = description;
        this.imageLink = imageLink;
        this.timestamp = timestamp;
        this.expiryDate = expiryDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.address = address;
    }

    public Provider() {

    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(double expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SearchTerm getLocation() {
        return location;
    }

    public void setLocation(SearchTerm location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
