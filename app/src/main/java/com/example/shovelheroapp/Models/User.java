package com.example.shovelheroapp.Models;

import android.media.Image;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.example.shovelheroapp.Models.Enums.AccountType;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class User {
    //MANDATORY FIELDS

    private int userId;
    private String accountType;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private CalendarView birthdate;
    private int age;
    private String email;
    private String phoneNo;


    //NON-CONSTRUCTOR FIELDS
    private int paymentId;  // -->Foreign key - to choose from List of properties
    private int addressId; //-->Foreign key - to choose from list of properties
    private Image guardianId; //on Guardian view only
    private CheckBox guardianIdValidated; // --> only available to app team
    private List<User> linkedShovellerId; // --> on Guardian view only
    private List<User> linkedGuardianId; // --> on YouthShoveller view only
    private int shovellerRadius; // --> how far shovelling are willing to walk


    public User(){}

    //CONSTRUCTOR
    public User(int userId, String accountType, String username, String password, String firstName, String lastName, CalendarView birthdate, String email, String phoneNo) {
        this.userId = userId;
        this.accountType = accountType;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNo = phoneNo;
    }


    //GETTERS AND SETTER
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getAccountType() {
        return accountType;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public CalendarView getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(CalendarView birthdate) {
        this.birthdate = birthdate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Image getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Image guardianId) {
        this.guardianId = guardianId;
    }

    public CheckBox getGuardianIdValidated() {
        return guardianIdValidated;
    }

    public void setGuardianIdValidated(CheckBox guardianIdValidated) {
        this.guardianIdValidated = guardianIdValidated;
    }

    public List<User> getLinkedShovellerId() {
        return linkedShovellerId;
    }

    public void setLinkedShovellerId(List<User> linkedShovellerId) {
        this.linkedShovellerId = linkedShovellerId;
    }

    public List<User> getLinkedGuardianId() {
        return linkedGuardianId;
    }

    public void setLinkedGuardianId(List<User> linkedGuardianId) {
        this.linkedGuardianId = linkedGuardianId;
    }

    public int getShovellerRadius() {
        return shovellerRadius;
    }

    public void setShovellerRadius(int shovellerRadius) {
        this.shovellerRadius = shovellerRadius;
    }
}
