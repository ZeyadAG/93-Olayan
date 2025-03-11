package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    private static final String ORDERS_JSON_PATH = "data/orders.json"; // JSON file path

    public OrderRepository() {
    }

    @Override
    protected String getDataPath() {
        return ORDERS_JSON_PATH;
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class; // Correct type for JSON deserialization
    }


    public void addOrder(Order order) {
        save(order);
    }


    public ArrayList<Order> getOrders() {
        return findAll();
    }


    public Order getOrderById(UUID orderId) {
        return getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }


    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = getOrders();
        orders.removeIf(order -> order.getId().equals(orderId));
        overrideData(orders); // Save updated list
    }
}
