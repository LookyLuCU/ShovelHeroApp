package com.example.shovelheroapp.User.Customer;

import com.example.shovelheroapp.Billing.Payment;
import com.example.shovelheroapp.User.Address;

public class Customer {
    private long customerId;
    private String customerUsername;
    private String customerPassword;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhoneNo;
    private Payment customerPaymentId;  // -->Foreign key
    private Address customerAddressId; //-->TO choose from list of properties


    public Customer(){

    }


    //CONSTRUCTOR
    public Customer(long customerId, String customerUsername, String customerPassword, String customerFirstName, String customerLastName, String customerEmail, String customerPhoneNo, Payment customerPaymentId, Address customerAddressId) {
        this.customerId = customerId;
        this.customerUsername = customerUsername;
        this.customerPassword = customerPassword;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPhoneNo = customerPhoneNo;
        this.customerPaymentId = customerPaymentId;
        this.customerAddressId = customerAddressId;
    }

    //Getters and setters
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public Payment getCustomerPaymentId() {
        return customerPaymentId;
    }

    public void setCustomerPaymentId(Payment customerPaymentId) {
        this.customerPaymentId = customerPaymentId;
    }

    public Address getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(Address customerAddressId) {
        this.customerAddressId = customerAddressId;
    }
}
