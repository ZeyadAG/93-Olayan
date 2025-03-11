package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.UserRepository;
import com.example.repository.OrderRepository;
import com.example.repository.CartRepository;
import com.example.service.UserService;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private UUID userId;
    private Order testOrder;
    private Cart testCart;
    private Product testProduct;
    private UUID cartId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User(userId, "John Doe", new ArrayList<>());
        testOrder = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        cartId = UUID.randomUUID();
        testCart = new Cart(cartId, userId, new ArrayList<>());
        testProduct = new Product(UUID.randomUUID(), "Laptop", 1200.0);
    }

    @Test
    void testAddUser() {
        when(userRepository.addUser(any(User.class))).thenReturn(testUser);
        User savedUser = userService.addUser(testUser);
        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        verify(userRepository, times(1)).addUser(any(User.class));
    }

    @Test
    void testGetUsers() {
        List<User> userList = List.of(testUser);
        when(userRepository.getUsers()).thenReturn(new ArrayList<>(userList));
        List<User> users = userService.getUsers();
        assertEquals(1, users.size());
        assertEquals(testUser.getId(), users.get(0).getId());
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    void testGetUserById() {
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        User retrievedUser = userService.getUserById(userId);
        assertNotNull(retrievedUser);
        assertEquals(testUser.getId(), retrievedUser.getId());
        verify(userRepository, times(1)).getUserById(userId);
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteUserById(userId);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }

    @Test
    void testUserExists() {
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        boolean exists = userService.userExists(userId);
        assertTrue(exists);
        verify(userRepository, times(1)).getUserById(userId);
    }

    @Test
    void testUserDoesNotExist() {
        when(userRepository.getUserById(userId)).thenReturn(null);
        boolean exists = userService.userExists(userId);
        assertFalse(exists);
        verify(userRepository, times(1)).getUserById(userId);
    }

    @Test
    void testGetOrdersByUserId() {
        List<Order> orderList = List.of(testOrder);
        when(userRepository.getOrdersByUserId(userId)).thenReturn(orderList);
        List<Order> orders = userService.getOrdersByUserId(userId);
        assertEquals(1, orders.size());
        assertEquals(testOrder.getId(), orders.get(0).getId());
        verify(userRepository, times(1)).getOrdersByUserId(userId);
    }

    @Test
    void testRemoveOrderFromUser() {
        doNothing().when(userRepository).removeOrderFromUser(userId, testOrder.getId());
        doNothing().when(orderRepository).deleteOrderById(testOrder.getId());
        userService.removeOrderFromUser(userId, testOrder.getId());
        verify(userRepository, times(1)).removeOrderFromUser(userId, testOrder.getId());
        verify(orderRepository, times(1)).deleteOrderById(testOrder.getId());
    }

    @Test
    void testAddCart() {
        doNothing().when(cartRepository).addCart(any(Cart.class)); // Use doNothing() for void methods
        Cart savedCart = cartService.addCart(testCart);
        assertNotNull(savedCart);
        assertEquals(testCart.getId(), savedCart.getId());
        verify(cartRepository, times(1)).addCart(any(Cart.class));
    }


    @Test
    void testGetCartByUserId() {
        when(cartRepository.getCartByUserId(userId)).thenReturn(testCart);
        Cart retrievedCart = cartService.getCartByUserId(userId);
        assertNotNull(retrievedCart);
        assertEquals(testCart.getId(), retrievedCart.getId());
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }

    @Test
    void testAddProductToCart() {
        when(cartRepository.getCartById(cartId)).thenReturn(testCart);
        doNothing().when(cartRepository).updateCart(eq(cartId), any(Cart.class));
        cartService.addProductToCart(cartId, testProduct);
        verify(cartRepository, times(1)).updateCart(eq(cartId), any(Cart.class));
    }

    @Test
    void testDeleteProductFromCart() {
        testCart.getProducts().add(testProduct);
        when(cartRepository.getCartById(cartId)).thenReturn(testCart);
        doNothing().when(cartRepository).updateCart(eq(cartId), any(Cart.class));
        cartService.deleteProductFromCart(cartId, testProduct);
        verify(cartRepository, times(1)).updateCart(eq(cartId), any(Cart.class));
    }

    @Test
    void testDeleteCartById() {
        doNothing().when(cartRepository).deleteCartById(cartId);
        cartService.deleteCartById(cartId);
        verify(cartRepository, times(1)).deleteCartById(cartId);
    }
}
