package com.example.shovelheroapp.Models;

import com.example.shovelheroapp.Models.Enums.PaymentProviders;
import com.example.shovelheroapp.Models.Enums.PaymentStatus;

import java.util.Date;
import java.util.List;

public class Payment {
    private long paymentID;
    private String paymentType;
    private List<PaymentProviders> paymentProvider;
    private int paymentCardNo;
    private Date paymentExpiry;
    private int paymentCCV;
    private WorkOrder jobPrice;
    private PaymentStatus paymentApproved;
}
