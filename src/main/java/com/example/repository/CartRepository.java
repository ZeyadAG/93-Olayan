package com.example.repository;

import com.example.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class CartRepository {

    private final ArrayList<Cart> carts = new ArrayList<>();


    public void addCart(Cart cart) {
        carts.add(cart);
    }


    public ArrayList<Cart> getCarts() {
        return carts;
    }


    public Cart getCartById(UUID cartId) {
        return carts.stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }


    public Cart getCartByUserId(UUID userId) {
        return carts.stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }


    public void updateCart(UUID cartId, Cart updatedCart) {
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getId().equals(cartId)) {
                carts.set(i, updatedCart);  // Replace old cart with updated one
                return;
            }
        }
    }


    public void deleteCartById(UUID cartId) {
        carts.removeIf(cart -> cart.getId().equals(cartId));
    }
}
