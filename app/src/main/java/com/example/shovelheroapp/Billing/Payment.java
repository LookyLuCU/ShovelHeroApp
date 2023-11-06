package com.example.shovelheroapp.Billing;

import java.util.Date;
import java.util.List;

public class Payment {
    private long paymentID;
    private String paymentType;
    private List<PaymentProviders> paymentProvider;
    private int paymentCardNo;
    private Date paymentExpiry;
    private int paymentCCV;
    private PaymentStatus paymentApproved;

}
