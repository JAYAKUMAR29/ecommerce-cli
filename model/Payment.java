package com.ecommerce.models;

public class Payment {

    private int paymentId;
    private int orderId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;

    public Payment(int paymentId, int orderId, double amount,
                   PaymentMethod method, PaymentStatus status,
                   String transactionId) {

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionId = transactionId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}