package com.example.service;

import org.springframework.stereotype.Service;
import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.repository.UserRepository;
import com.example.repository.OrderRepository;
import com.example.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public User addUser(User user) {
        userRepository.addUser(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        Cart userCart = cartRepository.getCartByUserId(userId);

        if (userCart != null && !userCart.getProducts().isEmpty()) {
            double totalPrice = userCart.getProducts().stream().mapToDouble(p -> p.getPrice()).sum();
            Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, userCart.getProducts());

            orderRepository.addOrder(newOrder);
            userRepository.addOrderToUser(userId, newOrder);
            emptyCart(userId);
        }
    }

    public void emptyCart(UUID userId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            userCart.getProducts().clear();
            cartRepository.addCart(userCart);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
        orderRepository.deleteOrderById(orderId);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
    }
}
