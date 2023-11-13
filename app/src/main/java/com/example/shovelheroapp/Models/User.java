package com.example.shovelheroapp.Models;

import android.media.Image;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.example.shovelheroapp.Models.Enums.AccountType;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    //MANDATORY FIELDS

    private String userId;
    private String accountType;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthdate; // For Firebase data compatibility
    private int age;
    private String email;
    private String phoneNo;
    private List<Address1> addresses;


    //NON-CONSTRUCTOR FIELDS
    private int paymentId;  // -->Foreign key - to choose from List of properties
    private int addressId; //-->Foreign key - to choose from list of properties
    private String  guardianIdUrl; //on Guardian view only
    private boolean guardianIdValidated; // --> only available to app team // I don't think Firebase works with complex Android UI (boolean)?

    private List<String> linkedShovellerId; // --> on Guardian view only  List<String> ? Is this correct?
    private List<String> linkedGuardianId; // --> on YouthShoveller view only
    private int shovellerRadius; // --> how far shovelling are willing to walk


    public User(){}

    //CONSTRUCTOR
    public User(String userId, String accountType, String username, String password, String firstName, String lastName, String birthdate, String email, String phoneNo) {
        this.userId = userId;
        this.accountType = accountType;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNo = phoneNo;
        this.addresses = new ArrayList<>();
    }



    //GETTERS AND SETTER
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
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

    public List<Address1> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address1> addresses) {
        this.addresses = addresses;
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

    public String getGuardianId() {
        return guardianIdUrl;
    }

    public void setGuardianId(String guardianIdUrl) {
        this.guardianIdUrl = guardianIdUrl;
    }

    public boolean getGuardianIdValidated() {
        return guardianIdValidated;
    }

    public void setGuardianIdValidated(boolean guardianIdValidated) {
        this.guardianIdValidated = guardianIdValidated;
    }

    public List<String> getLinkedShovellerId() {
        return linkedShovellerId;
    }

    public void setLinkedShovellerId(List<String> linkedShovellerId) {
        this.linkedShovellerId = linkedShovellerId;
    }

    public List<String> getLinkedGuardianId() {
        return linkedGuardianId;
    }

    public void setLinkedGuardianId(List<String> linkedGuardianId) {
        this.linkedGuardianId = linkedGuardianId;
    }

    public int getShovellerRadius() {
        return shovellerRadius;
    }

    public void setShovellerRadius(int shovellerRadius) {
        this.shovellerRadius = shovellerRadius;
    }


    public static class Address1 {
        private String addressId1;
        //private Image customerAddressImage;
        private String address1;
        private String city1;
        private String province1;
        private String postalCode1;
        private String country1;
        private String addressNotes1;
        private int drivewaySquareFootage1;
        private String accessibleOk1;
        private String shovelAvailable1;


        public Address1() {}

        //CONSTRUCTOR


        public Address1(String addressId1, String address1, String city1, String province1, String postalCode1, String country1, String addressNotes1, int drivewaySquareFootage1, String accessibleOk1, String shovelAvailable1) {
            this.addressId1 = addressId1;
            this.address1 = address1;
            this.city1 = city1;
            this.province1 = province1;
            this.postalCode1 = postalCode1;
            this.country1 = country1;
            this.addressNotes1 = addressNotes1;
            this.drivewaySquareFootage1 = drivewaySquareFootage1;
            this.accessibleOk1 = accessibleOk1;
            this.shovelAvailable1 = shovelAvailable1;
        }

        public String getAddressId1() {
            return addressId1;
        }

        public void setAddressId1(String addressId1) {
            this.addressId1 = addressId1;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getCity1() {
            return city1;
        }

        public void setCity1(String city1) {
            this.city1 = city1;
        }

        public String getProvince1() {
            return province1;
        }

        public void setProvince1(String province1) {
            this.province1 = province1;
        }

        public String getPostalCode1() {
            return postalCode1;
        }

        public void setPostalCode1(String postalCode1) {
            this.postalCode1 = postalCode1;
        }

        public String getCountry1() {
            return country1;
        }

        public void setCountry1(String country1) {
            this.country1 = country1;
        }

        public String getAddressNotes1() {
            return addressNotes1;
        }

        public void setAddressNotes1(String addressNotes1) {
            this.addressNotes1 = addressNotes1;
        }

        public int getDrivewaySquareFootage1() {
            return drivewaySquareFootage1;
        }

        public void setDrivewaySquareFootage1(int drivewaySquareFootage1) {
            this.drivewaySquareFootage1 = drivewaySquareFootage1;
        }

        public String getAccessibleOk1() {
            return accessibleOk1;
        }

        public void setAccessibleOk1(String accessibleOk1) {
            this.accessibleOk1 = accessibleOk1;
        }

        public String getShovelAvailable1() {
            return shovelAvailable1;
        }

        public void setShovelAvailable1(String shovelAvailable1) {
            this.shovelAvailable1 = shovelAvailable1;
        }
    }
}
