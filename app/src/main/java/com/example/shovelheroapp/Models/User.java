package com.example.shovelheroapp.Models;

import java.util.ArrayList;
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
    private List<Address> addresses;


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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
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


    public static class Address {
        private String addressId;
        //private Image customerAddressImage;
        private String address;
        private String city;
        private String province;
        private String postalCode;
        private String country;
        private String addressNotes;
        private int drivewaySquareFootage;
        private String accessibleOk;
        private String shovelAvailable;


        public Address() {}

        //CONSTRUCTOR


        public Address(String addressId, String address, String city, String province, String postalCode, String country, String addressNotes, int drivewaySquareFootage, String accessibleOk, String shovelAvailable) {
            this.addressId = addressId;
            this.address = address;
            this.city = city;
            this.province = province;
            this.postalCode = postalCode;
            this.country = country;
            this.addressNotes = addressNotes;
            this.drivewaySquareFootage = drivewaySquareFootage;
            this.accessibleOk = accessibleOk;
            this.shovelAvailable = shovelAvailable;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
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

        public int getDrivewaySquareFootage() {
            return drivewaySquareFootage;
        }

        public void setDrivewaySquareFootage(int drivewaySquareFootage) {
            this.drivewaySquareFootage = drivewaySquareFootage;
        }

        public String getAccessibleOk() {
            return accessibleOk;
        }

        public void setAccessibleOk(String accessibleOk) {
            this.accessibleOk = accessibleOk;
        }

        public String getShovelAvailable() {
            return shovelAvailable;
        }

        public void setShovelAvailable(String shovelAvailable) {
            this.shovelAvailable = shovelAvailable;
        }
    }
}
