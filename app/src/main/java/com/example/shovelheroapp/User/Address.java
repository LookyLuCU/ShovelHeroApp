package com.example.shovelheroapp.User;

import android.media.Image;
import android.widget.CheckBox;

public class Address {
    private long addressId;
    private Image customerAddressImage;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String addressNotes;
    private String drivewaySquarefootage;
    private CheckBox accessible;
    private CheckBox shovelAvailableOnSite;


    //CONSTRUCTOR
    public Address(long addressId, Image customerAddressImage, String address, String city, String province, String postalCode, String country, String addressNotes, String drivewaySquarefootage, CheckBox accessible, CheckBox shovelAvailableOnSite) {
        this.addressId = addressId;
        this.customerAddressImage = customerAddressImage;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.addressNotes = addressNotes;
        this.drivewaySquarefootage = drivewaySquarefootage;
        this.accessible = accessible;
        this.shovelAvailableOnSite = shovelAvailableOnSite;
    }


    //GETTERS AND SETTERS
    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public Image getCustomerAddressImage() {
        return customerAddressImage;
    }

    public void setCustomerAddressImage(Image customerAddressImage) {
        this.customerAddressImage = customerAddressImage;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressNotes() {
        return addressNotes;
    }

    public void setAddressNotes(String addressNotes) {
        this.addressNotes = addressNotes;
    }

    public String getDrivewaySquarefootage() {
        return drivewaySquarefootage;
    }

    public void setDrivewaySquarefootage(String drivewaySquarefootage) {
        this.drivewaySquarefootage = drivewaySquarefootage;
    }

    public CheckBox getAccessible() {
        return accessible;
    }

    public void setAccessible(CheckBox accessible) {
        this.accessible = accessible;
    }

    public CheckBox getShovelAvailableOnSite() {
        return shovelAvailableOnSite;
    }

    public void setShovelAvailableOnSite(CheckBox shovelAvailableOnSite) {
        this.shovelAvailableOnSite = shovelAvailableOnSite;
    }
}
