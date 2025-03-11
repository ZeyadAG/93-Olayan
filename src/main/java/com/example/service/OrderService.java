package com.example.service;

import org.springframework.stereotype.Service;
import com.example.model.Order;
import com.example.repository.OrderRepository;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public boolean deleteOrderById(UUID orderId) throws IllegalArgumentException {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            return false;
        }
        orderRepository.deleteOrderById(orderId);
        return true;
    }
}
