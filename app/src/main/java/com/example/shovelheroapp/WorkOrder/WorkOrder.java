package com.example.shovelheroapp.WorkOrder;

import android.media.Image;

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


    //private Customer customerId; //to be foreign key
    //private Property propertyId; //to be foreign key
    //private Invoice invoiceId; --> is this how we do it? with foreign key?
    //private Transaction paymentId; --> is this how we do it? Transaction? with foreign key?


    public WorkOrder(){

    }
    public WorkOrder(long id, String orderType, String status) {
        this.workOrderId = id;
        this.orderType = orderType;
        this.status = status;
    }

    public long getId() {
        return workOrderId;
    }

    public void setId(long workOrderId) {
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
}
