package com.example.flamepro;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private List<CartItem> items;
    private String orderDate;
    private String estDelivery;
    private String totalPrice;
    private OrderStatus status;

    public enum OrderStatus {
        PENDING("Pending"),
        SHIPPED("Shipped"),
        DELIVERED("Delivered"),
        CANCELLED("Cancelled");

        private String label;
        OrderStatus(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public Order(String orderId, List<CartItem> items, String orderDate, String estDelivery, String totalPrice, OrderStatus status) {
        this.orderId = orderId;
        this.items = items;
        this.orderDate = orderDate;
        this.estDelivery = estDelivery;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public List<CartItem> getItems() { return items; }
    public String getOrderDate() { return orderDate; }
    public String getEstDelivery() { return estDelivery; }
    public String getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
