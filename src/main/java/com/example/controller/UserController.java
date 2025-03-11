package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    public UserController(ProductService productService, UserService userService, CartService cartService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> addOrderToUser(@PathVariable UUID userId) {
            userService.addOrderToUser(userId);
            return ResponseEntity.ok("Order added successfully");
        }

    @PostMapping("/{userId}/removeOrder")
    public ResponseEntity<String> removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return ResponseEntity.ok("Order removed successfully");
    }

    @DeleteMapping("/{userId}/emptyCart")
    public ResponseEntity<String> emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return ResponseEntity.ok("Cart emptied successfully");
    }

    @PutMapping("/addProductToCart")
    public ResponseEntity<String> addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = this.productService.getProductById(productId);
        Cart cart = cartService.getCartByUserId(userId);
        cartService.addProductToCart(cart.getId(), product);
        return ResponseEntity.ok("Product added to cart");
    }

    @PutMapping("/deleteProductFromCart")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = this.productService.getProductById(productId);
        Cart cart = cartService.getCartByUserId(userId);
        boolean flag = cartService.deleteProductFromCart(cart.getId(), product);
        if(flag){
            return ResponseEntity.ok("Product deleted from cart");
        }
        return ResponseEntity.ok("Cart is empty");

    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID userId) {
        if (!userService.userExists(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body("User not found");
        }
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }


}
