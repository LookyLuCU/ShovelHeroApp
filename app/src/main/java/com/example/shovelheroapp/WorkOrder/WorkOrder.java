package com.example.shovelheroapp.WorkOrder;

public class WorkOrder {


    private long id;
    private String customerId; //to be foreign key
    private String orderType;
    private String status;

    public WorkOrder(){

    }
    public WorkOrder(long id, String orderType, String status) {
        this.id = id;
        this.orderType = orderType;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
