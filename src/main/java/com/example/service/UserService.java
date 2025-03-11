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

    // Constructor with Dependency Injection
    public UserService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    // 7.2.2.1 Add New User
    public User addUser(User user) {
        userRepository.addUser(user);
        return user;
    }

    // 7.2.2.2 Get the Users
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    // 7.2.2.3 Get a Specific User
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    // 7.2.2.4 Get the User’s Orders
    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    // 7.2.2.5 Add a New Order (Checkout User's Cart)
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

    // 7.2.2.6 Empty Cart
    public void emptyCart(UUID userId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            userCart.getProducts().clear();
            cartRepository.addCart(userCart);
        }
    }

    // 7.2.2.7 Remove Order
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
        orderRepository.deleteOrderById(orderId);
    }

    // 7.2.2.8 Delete the User
    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
    }
}
