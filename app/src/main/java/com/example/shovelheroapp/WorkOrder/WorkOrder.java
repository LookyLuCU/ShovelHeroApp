package com.example.shovelheroapp.WorkOrder;

import android.media.Image;

import com.example.shovelheroapp.User.Address;
import com.example.shovelheroapp.User.Customer.Customer;

import java.util.Date;

public class WorkOrder {


    private long workOrderId;

    private String orderType;
    private String status;
    private double price;
    private Date requestDate;
    private Date requestedDateTime;
    private Image arrivalImage;
    private Image completedImage;
    private Image issueImage;


    private Customer customerId; //to be foreign key
    private Address propertyId; //to be foreign key
    //private Invoice invoiceId; --> is this how we do it? with foreign key?
    //private Transaction paymentId; --> is this how we do it? Transaction? with foreign key?


    public WorkOrder(){

    }

    public WorkOrder(long workOrderId, String orderType, String status, double price, Date requestDate, Date requestedDateTime, Customer customerId, Address propertyId) {
        this.workOrderId = workOrderId;
        this.orderType = orderType;
        this.status = status;
        this.price = price;
        this.requestDate = requestDate;
        this.requestedDateTime = requestedDateTime;
        this.customerId = customerId;
        this.propertyId = propertyId;
    }


    public long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getRequestedDateTime() {
        return requestedDateTime;
    }

    public void setRequestedDateTime(Date requestedDateTime) {
        this.requestedDateTime = requestedDateTime;
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

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Address getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Address propertyId) {
        this.propertyId = propertyId;
    }
}
