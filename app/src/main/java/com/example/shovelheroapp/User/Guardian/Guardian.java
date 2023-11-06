package com.example.shovelheroapp.User.Guardian;

import com.example.shovelheroapp.Billing.Payment;
import com.example.shovelheroapp.User.AccountType;
import com.example.shovelheroapp.User.Address;
import com.example.shovelheroapp.User.Shoveller.Shoveller;

import java.util.Date;
import java.util.List;

public class Guardian {
    private long guardianId;
    private AccountType accountTypeId; // -->Foreign key
    private String guardianUsername;
    private String guardianPassword;
    private Date guardianBirthdate;
    private int guardianAge;
    private String guardianFirstName;
    private String guardianLastName;
    private String guardianEmail;
    private String guardianPhoneNo;
    private Payment guardianPaymentId; // -->Foreign key
    private Address guardianAddressId; // -->Foreign key
    private List<Shoveller> shovellerId;

    public Guardian(){}

    //CONSTRUCTOR
    public Guardian(long guardianId, AccountType accountTypeId, String guardianUsername, String guardianPassword, Date guardianBirthdate, int guardianAge, String guardianFirstName, String guardianLastName, String guardianEmail, String guardianPhoneNo, Payment guardianPaymentId, Address guardianAddressId) {
        this.guardianId = guardianId;
        this.accountTypeId = accountTypeId;
        this.guardianUsername = guardianUsername;
        this.guardianPassword = guardianPassword;
        this.guardianBirthdate = guardianBirthdate;
        this.guardianAge = guardianAge;
        this.guardianFirstName = guardianFirstName;
        this.guardianLastName = guardianLastName;
        this.guardianEmail = guardianEmail;
        this.guardianPhoneNo = guardianPhoneNo;
        this.guardianPaymentId = guardianPaymentId;
        this.guardianAddressId = guardianAddressId;
    }

    public long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(long guardianId) {
        this.guardianId = guardianId;
    }

    public AccountType getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(AccountType accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getGuardianUsername() {
        return guardianUsername;
    }

    public void setGuardianUsername(String guardianUsername) {
        this.guardianUsername = guardianUsername;
    }

    public String getGuardianPassword() {
        return guardianPassword;
    }

    public void setGuardianPassword(String guardianPassword) {
        this.guardianPassword = guardianPassword;
    }

    public Date getGuardianBirthdate() {
        return guardianBirthdate;
    }

    public void setGuardianBirthdate(Date guardianBirthdate) {
        this.guardianBirthdate = guardianBirthdate;
    }

    public int getGuardianAge() {
        return guardianAge;
    }

    public void setGuardianAge(int guardianAge) {
        this.guardianAge = guardianAge;
    }

    public String getGuardianFirstName() {
        return guardianFirstName;
    }

    public void setGuardianFirstName(String guardianFirstName) {
        this.guardianFirstName = guardianFirstName;
    }

    public String getGuardianLastName() {
        return guardianLastName;
    }

    public void setGuardianLastName(String guardianLastName) {
        this.guardianLastName = guardianLastName;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getGuardianPhoneNo() {
        return guardianPhoneNo;
    }

    public void setGuardianPhoneNo(String guardianPhoneNo) {
        this.guardianPhoneNo = guardianPhoneNo;
    }

    public Payment getGuardianPaymentId() {
        return guardianPaymentId;
    }

    public void setGuardianPaymentId(Payment guardianPaymentId) {
        this.guardianPaymentId = guardianPaymentId;
    }

    public Address getGuardianAddressId() {
        return guardianAddressId;
    }

    public void setGuardianAddressId(Address guardianAddressId) {
        this.guardianAddressId = guardianAddressId;
    }

    public List<Shoveller> getShovellerId() {
        return shovellerId;
    }

    public void setShovellerId(List<Shoveller> shovellerId) {
        this.shovellerId = shovellerId;
    }
}
