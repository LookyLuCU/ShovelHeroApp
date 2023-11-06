package com.example.shovelheroapp.User.Shoveller;

import com.example.shovelheroapp.Billing.Payment;
import com.example.shovelheroapp.User.AccountType;
import com.example.shovelheroapp.User.Address;
import com.example.shovelheroapp.User.Guardian.Guardian;

import java.util.Date;
import java.util.List;

public class Shoveller {
    private long customerId;
    private AccountType accountType; // -->Foreign key
    private String shovellerUsername;
    private String shovellerPassword;
    private Date shovellerBirthdate;
    private int shovellerAge;
    private String shovellerFirstName;
    private String shovellerLastName;
    private String shovellerEmail;
    private String shovellerPhoneNo;
    private Payment paymentId;
    private Address shovellerAddressId;  // -->Foreign key
    private int maxKmsAllowed;
    private List<Guardian> linkedGuardian; //-- foreign key?

    public Shoveller(){}

    public Shoveller(long customerId, AccountType accountType, String shovellerUsername, String shovellerPassword, Date shovellerBirthdate, int shovellerAge, String shovellerFirstName, String shovellerLastName, String shovellerEmail, String shovellerPhoneNo, Payment paymentId, Address shovellerAddressId, int maxKmsAllowed) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.shovellerUsername = shovellerUsername;
        this.shovellerPassword = shovellerPassword;
        this.shovellerBirthdate = shovellerBirthdate;
        this.shovellerAge = shovellerAge;
        this.shovellerFirstName = shovellerFirstName;
        this.shovellerLastName = shovellerLastName;
        this.shovellerEmail = shovellerEmail;
        this.shovellerPhoneNo = shovellerPhoneNo;
        this.paymentId = paymentId;
        this.shovellerAddressId = shovellerAddressId;
        this.maxKmsAllowed = maxKmsAllowed;
    }


    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getShovellerUsername() {
        return shovellerUsername;
    }

    public void setShovellerUsername(String shovellerUsername) {
        this.shovellerUsername = shovellerUsername;
    }

    public String getShovellerPassword() {
        return shovellerPassword;
    }

    public void setShovellerPassword(String shovellerPassword) {
        this.shovellerPassword = shovellerPassword;
    }

    public Date getBirthdate() {
        return shovellerBirthdate;
    }

    public void setShovellerBirthdate(Date shovellerBirthdate) {
        this.shovellerBirthdate = shovellerBirthdate;
    }

    public int getShovellerAge() {
        return shovellerAge;
    }

    public void setShovellerAge(int shovellerAge) {
        this.shovellerAge = shovellerAge;
    }

    public String getShovellerFirstName() {
        return shovellerFirstName;
    }

    public void setShovellerFirstName(String shovellerFirstName) {
        this.shovellerFirstName = shovellerFirstName;
    }

    public String getShovellerLastName() {
        return shovellerLastName;
    }

    public void setShovellerLastName(String shovellerLastName) {
        this.shovellerLastName = shovellerLastName;
    }

    public String getShovellerEmail() {
        return shovellerEmail;
    }

    public void setShovellerEmail(String shovellerEmail) {
        this.shovellerEmail = shovellerEmail;
    }

    public String getShovellerPhoneNo() {
        return shovellerPhoneNo;
    }

    public void setShovellerPhoneNo(String shovellerPhoneNo) {
        this.shovellerPhoneNo = shovellerPhoneNo;
    }

    public Payment getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Payment paymentId) {
        this.paymentId = paymentId;
    }

    public Address getShovellerAddressId() {
        return shovellerAddressId;
    }

    public void setShovellerAddressId(Address shovellerAddressId) {
        this.shovellerAddressId = shovellerAddressId;
    }

    public int getMaxKmsAllowed() {
        return maxKmsAllowed;
    }

    public void setMaxKmsAllowed(int maxKmsAllowed) {
        this.maxKmsAllowed = maxKmsAllowed;
    }

    public  List<Guardian> getLinkedGuardian() {
        return linkedGuardian;
    }

    public void setLinkedGuardian(List<Guardian> linkedGuardian) {
        this.linkedGuardian = linkedGuardian;
    }
}
