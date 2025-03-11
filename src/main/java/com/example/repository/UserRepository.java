package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    private static final String USERS_JSON_PATH = "data/users.json"; // JSON file path

    public UserRepository() {
    }

    @Override
    protected String getDataPath() {
        return USERS_JSON_PATH;
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class; // Provide correct type for deserialization
    }


    public ArrayList<User> getUsers() {
        return findAll();
    }


    public User getUserById(UUID userId) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }


    public User addUser(User user) {
        save(user);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return (user != null) ? user.getOrders() : new ArrayList<>();
    }


    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            updateUser(user);
        }
    }


    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().removeIf(order -> order.getId().equals(orderId));
            updateUser(user);
        }
    }


    public void deleteUserById(UUID userId) {
        ArrayList<User> users = getUsers();
        users.removeIf(user -> user.getId().equals(userId));
        overrideData(users);
    }


    private void updateUser(User user) {
        ArrayList<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                break;
            }
        }
        overrideData(users);
    }
}
