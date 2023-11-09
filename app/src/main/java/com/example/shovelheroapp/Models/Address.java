package com.example.shovelheroapp.Models;

import android.media.Image;
import android.widget.CheckBox;

public class Address {
    private int addressId;
    //private Image customerAddressImage;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String addressNotes;
    private int drivewaySquareFootage;
    private CheckBox accessible;
    private String accessibleOk;
    private CheckBox shovelAvailableOnSite;
    private String shovelAvailable;



    //CONSTRUCTOR
    public Address(int addressId, String address, String city, String province, String postalCode, String country, String addressNotes, int drivewaySquarefootage, String accessibleOK, String shovelAvailable) {
        this.addressId = addressId;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.addressNotes = addressNotes;
        this.drivewaySquareFootage = drivewaySquarefootage;
        this.accessible = accessible;
        this.shovelAvailableOnSite = shovelAvailableOnSite;
    }


    //GETTERS AND SETTERS
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    /**
    public Image getCustomerAddressImage() {
        return customerAddressImage;
    }

    public void setCustomerAddressImage(Image customerAddressImage) {
        this.customerAddressImage = customerAddressImage;
    }
     **/

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

    public int getDrivewaySquareFootage() {
        return drivewaySquareFootage;
    }

    public void setDrivewaySquarefootage(int drivewaySquareFootage) {
        this.drivewaySquareFootage = drivewaySquareFootage;
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

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
