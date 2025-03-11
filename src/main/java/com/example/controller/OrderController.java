package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/")
    public ArrayList<Order> getOrders() {
        return orderService.getOrders();
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId) {
        boolean flag = orderService.deleteOrderById(orderId);
        if(flag){
            return "Order deleted successfully";
        }
        return "Order not found";

    }
}
