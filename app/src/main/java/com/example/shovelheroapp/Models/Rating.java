package com.example.shovelheroapp.Models;

import java.util.Date;

public class Rating {
    public long ratingId;
    public int rating;
    public String ratingCommments;
    public Date ratingDate;
    public Boolean ratingVisible;
    public User customerId;  // -->Foreign Key
    public User shovellerId; // -->Foreign Key


    public Rating(){
    }


    //CONSTRUCTOR
    public Rating(long ratingId, int rating, String ratingCommments, Date ratingDate, Boolean ratingVisible, User customerId, User shovellerId) {
        this.ratingId = ratingId;
        this.rating = rating;
        this.ratingCommments = ratingCommments;
        this.ratingDate = ratingDate;
        this.ratingVisible = ratingVisible;
        this.customerId = customerId;
        this.shovellerId = shovellerId;
    }


    //GETTERS AND SETTERS
    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingCommments() {
        return ratingCommments;
    }

    public void setRatingCommments(String ratingCommments) {
        this.ratingCommments = ratingCommments;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public Boolean getRatingVisible() {
        return ratingVisible;
    }

    public void setRatingVisible(Boolean ratingVisible) {
        this.ratingVisible = ratingVisible;
    }

    public User getCustomerId() {
        return customerId;
    }

    public void setCustomerId(User customerId) {
        this.customerId = customerId;
    }

    public User getShovellerId() {
        return shovellerId;
    }

    public void setShovellerId(User shovellerId) {
        this.shovellerId = shovellerId;
    }
}
