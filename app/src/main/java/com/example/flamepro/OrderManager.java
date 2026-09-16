package com.example.flamepro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
        // No mock data - list starts completely empty until user places a real order
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        // Add to the beginning so newest order is at the top
        orders.add(0, order);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> filtered = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() == status) {
                filtered.add(o);
            }
        }
        return filtered;
    }
}
