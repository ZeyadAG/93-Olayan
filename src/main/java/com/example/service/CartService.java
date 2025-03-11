package com.example.service;

import org.springframework.stereotype.Service;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    // Constructor with Dependency Injection
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 7.4.2.1 Add Cart
    public Cart addCart(Cart cart) {
        cartRepository.addCart(cart);
        return cart;
    }

    // 7.4.2.2 Get All Carts
    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    // 7.4.2.3 Get a Specific Cart
    public Cart getCartById(UUID cartId) {
        return cartRepository.getCartById(cartId);
    }

    // 7.4.2.4 Get a User’s Cart
    public Cart getCartByUserId(UUID userId) {
        return cartRepository.getCartByUserId(userId);
    }

    // 7.4.2.5 Add Product to the Cart
    public void addProductToCart(UUID cartId, Product product) {
        Cart cart = cartRepository.getCartById(cartId);
        if (cart != null) {
            cart.getProducts().add(product);
            cartRepository.updateCart(cartId, cart);
        }
    }

    // 7.4.2.6 Delete Product from the Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        Cart cart = cartRepository.getCartById(cartId);
        if (cart != null) {
            cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
            cartRepository.updateCart(cartId, cart);
        }
    }

    // 7.4.2.7 Delete the Cart
    public void deleteCartById(UUID cartId) {
        cartRepository.deleteCartById(cartId);
    }
}
