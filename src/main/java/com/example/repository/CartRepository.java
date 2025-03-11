package com.example.repository;

import com.example.model.Cart;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    private static final String CARTS_JSON_PATH = "src/main/java/com/example/data/carts.json";


    public CartRepository() {
    }

    @Override
    protected String getDataPath() {
        return CARTS_JSON_PATH;
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public void addCart(Cart cart) {
        save(cart);
    }

    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    public Cart getCartById(UUID cartId) {
        return getCarts().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId) {
        Cart userCart =  getCarts().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (userCart == null){
            Cart newCart = new Cart(userId);
            addCart(newCart);
            return newCart;
        }
        return userCart;
    }

    public void updateCart(UUID cartId, Cart updatedCart) {
        ArrayList<Cart> carts = getCarts();
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getId().equals(cartId)) {
                carts.set(i, updatedCart);
                overrideData(carts);
                return;
            }
        }
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = getCarts();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        overrideData(carts);
    }

    public void saveCart(ArrayList<Cart> carts) {
        try {
            objectMapper.writeValue(new File("src/main/java/com/example/data/carts.json"), carts);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }    }
}