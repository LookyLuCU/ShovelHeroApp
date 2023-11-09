package com.example.shovelheroapp.Models;

import android.media.Image;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.shovelheroapp.Models.Address;

import java.util.Date;

public class WorkOrder {

    private int workOrderId;
    private int customerId; //to be foreign key
    private int customerAddressId; //to be foreign key
    private String status;
    private int squareFootage;
    private double price;
    private Date requestDate;
    private DatePicker requestDatePicker;
    private TimePicker requestTimePicker;
    private Date requestedDateTime;
    private boolean isDrivewayChecked;
    private boolean isWalkwayChecked;
    private boolean isSidewalkChecked;
    private String specialInstructions;
    private Image arrivalImage;
    private Image completedImage;
    private Image issueImage;


    //private Invoice invoiceId; --> is this how we do it? with foreign key?
    //private Transaction paymentId; --> is this how we do it? Transaction? with foreign key?


    public WorkOrder(){
    }


    //CONSTRUCTOR
    public WorkOrder(int workOrderId, Date requestDate, String status, int squareFootage, boolean isDrivewayChecked, boolean isWalkwayChecked, boolean isSidewalkChecked, int customerId,, int customerAddressId) {
        this.workOrderId = workOrderId;
        this.requestDate = requestDate;
        this.status = status;
        this.squareFootage = squareFootage;
        this.isDrivewayChecked = isDrivewayChecked;
        this.isWalkwayChecked = isWalkwayChecked;
        this.isSidewalkChecked = isSidewalkChecked;
        this.customerId = customerId;
        this.customerAddressId = customerAddressId;
    }


    //GETTERS AND SETTERS
    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSquareFootage() { return squareFootage;
    }
    public void setSquareFootage(int squareFootage) { this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public DatePicker getRequestDatePicker() {
        return requestDatePicker;
    }

    public void setRequestDatePicker(DatePicker requestDatePicker) {
        this.requestDatePicker = requestDatePicker;
    }

    public TimePicker getRequestTimePicker() {
        return requestTimePicker;
    }

    public void setRequestTimePicker(TimePicker requestTimePicker) {
        this.requestTimePicker = requestTimePicker;
    }

    public Date getRequestedDateTime() {
        return requestedDateTime;
    }

    public void setRequestedDateTime(Date requestedDateTime) {
        this.requestedDateTime = requestedDateTime;
    }

    public boolean isDrivewayChecked() {
        return isDrivewayChecked;
    }

    public void setDrivewayChecked(boolean drivewayChecked) {
        isDrivewayChecked = drivewayChecked;
    }

    public boolean isWalkwayChecked() {
        return isWalkwayChecked;
    }

    public void setWalkwayChecked(boolean walkwayChecked) {
        isWalkwayChecked = walkwayChecked;
    }

    public boolean isSidewalkChecked() {
        return isSidewalkChecked;
    }

    public void setSidewalkChecked(boolean sidewalkChecked) {
        isSidewalkChecked = sidewalkChecked;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Image getArrivalImage() {
        return arrivalImage;
    }

    public void setArrivalImage(Image arrivalImage) {
        this.arrivalImage = arrivalImage;
    }

    public Image getCompletedImage() {
        return completedImage;
    }

    public void setCompletedImage(Image completedImage) {
        this.completedImage = completedImage;
    }

    public Image getIssueImage() {
        return issueImage;
    }

    public void setIssueImage(Image issueImage) {
        this.issueImage = issueImage;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int addressId) {
        AddressId = addressId;
    }
}
