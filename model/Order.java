package com.ecommerce.models;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;

    public Order(int orderId, int userId,
                 List<OrderItem> items,
                 OrderStatus status) {

        this.orderId = orderId;
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.totalAmount = calculateTotal();
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        this.totalAmount = calculateTotal();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double calculateTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        totalAmount = total;

        return total;
    }
}