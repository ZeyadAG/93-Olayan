package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public ResponseEntity<Cart> addCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.addCart(cart));
    }

    @GetMapping("/")
    public ResponseEntity<List<Cart>> getCarts() {
        return ResponseEntity.ok(cartService.getCarts());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable UUID cartId) {
        Cart cart = cartService.getCartById(cartId);
        return (cart != null) ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    @PutMapping("/addProductToCart")
    public ResponseEntity<String> addProductToCart(@RequestParam UUID userId, @RequestBody Product product) {
        cartService.addProductToCart(userId, product);
        return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestBody Product product) {
        cartService.deleteProductFromCart(userId, product);
        return "Product removed from cart successfully";
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId) {
        cartService.deleteCartById(cartId);
        return "Cart deleted successfully";
    }
}
