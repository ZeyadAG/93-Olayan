package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.repository.OrderRepository;
import com.example.repository.CartRepository;
import com.example.service.OrderService;
import com.example.service.ProductService;
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

    @InjectMocks private OrderService orderService;
    @Mock private ProductRepository productRepository;
    @InjectMocks private ProductService productService;

    private Product testProduct;
    private UUID productId;

    private Order testOrder;
    private UUID orderId;

    private User testUser;
    private UUID userId;
    private Cart testCart;
    private UUID cartId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User(userId, "John Doe", new ArrayList<>());
        testOrder = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        cartId = UUID.randomUUID();
        testCart = new Cart(cartId, userId, new ArrayList<>());
        productId = UUID.randomUUID(); // Ensure productId is always assigned
        productId = UUID.randomUUID();
        testProduct = new Product(productId, "Laptop", 1200.0);
    }

    @Test
    void testUserDoesNotExist() {
        when(userRepository.getUserById(userId)).thenReturn(null);
        boolean exists = userService.userExists(userId);
        assertFalse(exists);
        verify(userRepository, times(1)).getUserById(userId);
    }

    // addCart()
    @Test void testAddCart() { doNothing().when(cartRepository).addCart(testCart); assertNotNull(cartService.addCart(testCart)); }
    @Test void testAddCart_VerifyRepoCall() { cartService.addCart(testCart); verify(cartRepository, times(1)).addCart(testCart); }
    @Test void testAddCart_NotNull() { assertNotNull(cartService.addCart(testCart)); }

    // getCarts()
    @Test void testGetCarts() { when(cartRepository.getCarts()).thenReturn(new ArrayList<>(List.of(testCart))); assertEquals(1, cartService.getCarts().size()); }
    @Test void testGetCarts_Empty() { when(cartRepository.getCarts()).thenReturn(new ArrayList<>()); assertTrue(cartService.getCarts().isEmpty()); }
    @Test void testGetCarts_VerifyCall() { cartService.getCarts(); verify(cartRepository, times(1)).getCarts(); }

    // getCartById() with friend’s style
    @Test void getCartByID_shouldHaveCorrectUserId() {
        when(cartRepository.getCartById(cartId)).thenReturn(testCart);
        Cart retrievedCart = cartService.getCartById(cartId);
        assertEquals(userId, retrievedCart.getUserId(), "Cart fetched by ID should have matching userID");
    }

    @Test void getCartByID_shouldReturnNullForInvalidCartId() {
        UUID invalidCartID = UUID.randomUUID();
        when(cartRepository.getCartById(invalidCartID)).thenReturn(null);
        Cart retrievedCart = cartService.getCartById(invalidCartID);
        assertNull(retrievedCart, "Should return null for an invalid cart ID");
    }

    @Test void testGetCartById_VerifyCall() { cartService.getCartById(cartId); verify(cartRepository, times(1)).getCartById(cartId); }

    // getCartByUserId()
    @Test void testGetCartByUserId() { when(cartRepository.getCartByUserId(userId)).thenReturn(testCart); assertEquals(testCart, cartService.getCartByUserId(userId)); }
    @Test void testGetCartByUserId_Null() { when(cartRepository.getCartByUserId(userId)).thenReturn(null); assertNull(cartService.getCartByUserId(userId)); }
    @Test void testGetCartByUserId_VerifyCall() { cartService.getCartByUserId(userId); verify(cartRepository, times(1)).getCartByUserId(userId); }

    // addProductToCart()
    @Test void testAddProductToCart() { when(cartRepository.getCartById(cartId)).thenReturn(testCart); cartService.addProductToCart(cartId, testProduct); verify(cartRepository).updateCart(cartId, testCart); }
    @Test void testAddProductToCart_VerifyUpdateCall() { when(cartRepository.getCartById(cartId)).thenReturn(testCart); cartService.addProductToCart(cartId, testProduct); verify(cartRepository, times(1)).updateCart(cartId, testCart); }
    @Test void testAddProductToCart_CartNull() { when(cartRepository.getCartById(cartId)).thenReturn(null); cartService.addProductToCart(cartId, testProduct); verify(cartRepository, never()).updateCart(any(), any()); }

    // deleteProductFromCart()
    @Test void testDeleteProductFromCart() { testCart.getProducts().add(testProduct); when(cartRepository.getCartById(cartId)).thenReturn(testCart); assertTrue(cartService.deleteProductFromCart(cartId, testProduct)); }
    @Test void testDeleteProductFromCart_CartEmpty() { when(cartRepository.getCartById(cartId)).thenReturn(testCart); assertFalse(cartService.deleteProductFromCart(cartId, testProduct)); }
    @Test void testDeleteProductFromCart_CartNull() { when(cartRepository.getCartById(cartId)).thenReturn(null); assertFalse(cartService.deleteProductFromCart(cartId, testProduct)); }

    // deleteCartById()
    @Test void testDeleteCartById() { doNothing().when(cartRepository).deleteCartById(cartId); cartService.deleteCartById(cartId); verify(cartRepository).deleteCartById(cartId); }
    @Test void testDeleteCartById_VerifyCall() { cartService.deleteCartById(cartId); verify(cartRepository, times(1)).deleteCartById(cartId); }
    @Test void testDeleteCartById_NeverCall() { cartService.deleteCartById(cartId); verify(cartRepository, never()).deleteCartById(UUID.randomUUID()); }


    // addOrder()
    @Test void testAddOrder() { doNothing().when(orderRepository).addOrder(testOrder); orderService.addOrder(testOrder); verify(orderRepository, times(1)).addOrder(testOrder); }
    @Test void testAddOrder_VerifyCall() { orderService.addOrder(testOrder); verify(orderRepository, times(1)).addOrder(testOrder); }
    @Test void testAddOrder_NotNull() { assertNotNull(testOrder); }

    // getOrders()
    @Test void testGetOrders() { when(orderRepository.getOrders()).thenReturn(new ArrayList<>(List.of(testOrder))); assertEquals(1, orderService.getOrders().size()); }
    @Test void testGetOrders_Empty() { when(orderRepository.getOrders()).thenReturn(new ArrayList<>()); assertTrue(orderService.getOrders().isEmpty()); }
    @Test void testGetOrders_VerifyCall() { orderService.getOrders(); verify(orderRepository, times(1)).getOrders(); }

    // getOrderById()
    @Test void testGetOrderById() { when(orderRepository.getOrderById(orderId)).thenReturn(testOrder); assertEquals(testOrder, orderService.getOrderById(orderId)); }
    @Test void testGetOrderById_Null() { when(orderRepository.getOrderById(orderId)).thenReturn(null); assertNull(orderService.getOrderById(orderId)); }
    @Test void testGetOrderById_VerifyCall() { orderService.getOrderById(orderId); verify(orderRepository, times(1)).getOrderById(orderId); }

    // deleteOrderById()
    @Test void testDeleteOrderById() { when(orderRepository.getOrderById(orderId)).thenReturn(testOrder); doNothing().when(orderRepository).deleteOrderById(orderId); assertTrue(orderService.deleteOrderById(orderId)); }
    @Test void testDeleteOrderById_NotFound() { when(orderRepository.getOrderById(orderId)).thenReturn(null); assertFalse(orderService.deleteOrderById(orderId)); }
    @Test void testDeleteOrderById_VerifyCall() { when(orderRepository.getOrderById(orderId)).thenReturn(testOrder); orderService.deleteOrderById(orderId); verify(orderRepository, times(1)).deleteOrderById(orderId); }

    // addProduct()
    @Test void testAddProduct() { when(productRepository.addProduct(any(Product.class))).thenReturn(testProduct); assertNotNull(productService.addProduct(testProduct)); }
    @Test void testAddProduct_VerifyCall() { productService.addProduct(testProduct); verify(productRepository, times(1)).addProduct(testProduct); }
    @Test void testAddProduct_NotNull() { assertNotNull(productService.addProduct(testProduct)); }

    // getProducts()
    @Test void testGetProducts() { when(productRepository.getProducts()).thenReturn(new ArrayList<>(List.of(testProduct))); assertEquals(1, productService.getProducts().size()); }
    @Test void testGetProducts_Empty() { when(productRepository.getProducts()).thenReturn(new ArrayList<>()); assertTrue(productService.getProducts().isEmpty()); }
    @Test void testGetProducts_VerifyCall() { productService.getProducts(); verify(productRepository, times(1)).getProducts(); }

    // getProductById()
    @Test void testGetProductById() { when(productRepository.getProductById(productId)).thenReturn(testProduct); assertEquals(testProduct, productService.getProductById(productId)); }
    @Test void testGetProductById_Null() { when(productRepository.getProductById(productId)).thenReturn(null); assertNull(productService.getProductById(productId)); }
    @Test void testGetProductById_VerifyCall() { productService.getProductById(productId); verify(productRepository, times(1)).getProductById(productId); }

    // updateProduct()
    @Test void testUpdateProduct() { when(productRepository.getProductById(productId)).thenReturn(testProduct); productService.updateProduct(productId, "Updated Laptop", 1000.0); verify(productRepository).updateProduct(productId, "Updated Laptop", 1000.0); }
    @Test void testUpdateProduct_NotFound() { when(productRepository.getProductById(productId)).thenReturn(null); assertNull(productService.updateProduct(productId, "Updated Laptop", 1000.0)); }
    @Test void testUpdateProduct_VerifyCall() { when(productRepository.getProductById(productId)).thenReturn(testProduct); productService.updateProduct(productId, "Updated Laptop", 1000.0); verify(productRepository, times(1)).updateProduct(productId, "Updated Laptop", 1000.0); }

    // applyDiscount()
    @Test void testApplyDiscount() { when(productRepository.getProductById(productId)).thenReturn(testProduct); productService.applyDiscount(10, new ArrayList<>(new ArrayList<>(new ArrayList<>(List.of(productId))))); verify(productRepository).updateProduct(productId, testProduct.getName(), 1080.0); }
    @Test void testApplyDiscount_ProductNotFound() { when(productRepository.getProductById(productId)).thenReturn(null); productService.applyDiscount(10, new ArrayList<>(new ArrayList<>(List.of(productId)))); verify(productRepository, never()).updateProduct(any(), any(), anyDouble()); }
    @Test void testApplyDiscount_VerifyCall() { when(productRepository.getProductById(productId)).thenReturn(testProduct); productService.applyDiscount(10, new ArrayList<>(new ArrayList<>(List.of(productId)))); verify(productRepository, times(1)).updateProduct(productId, testProduct.getName(), 1080.0); }

    // deleteProductById()
    @Test void testDeleteProductById() { doNothing().when(productRepository).deleteProductById(productId); productService.deleteProductById(productId); verify(productRepository).deleteProductById(productId); }
    @Test void testDeleteProductById_VerifyCall() { productService.deleteProductById(productId); verify(productRepository, times(1)).deleteProductById(productId); }
    @Test void testDeleteProductById_NeverCall() { productService.deleteProductById(productId); verify(productRepository, never()).deleteProductById(UUID.randomUUID()); }

    // addUser()
    @Test void testAddUser() { when(userRepository.addUser(any(User.class))).thenReturn(testUser); assertNotNull(userService.addUser(testUser)); }
    @Test void testAddUser_VerifyCall() { userService.addUser(testUser); verify(userRepository, times(1)).addUser(testUser); }
    @Test void testAddUser_NotNull() { assertNotNull(userService.addUser(testUser)); }

    // getUsers()
    @Test void testGetUsers() { when(userRepository.getUsers()).thenReturn(new ArrayList<>(List.of(testUser))); assertEquals(1, userService.getUsers().size()); }
    @Test void testGetUsers_Empty() { when(userRepository.getUsers()).thenReturn(new ArrayList<>()); assertTrue(userService.getUsers().isEmpty()); }
    @Test void testGetUsers_VerifyCall() { userService.getUsers(); verify(userRepository, times(1)).getUsers(); }

    // getUserById()
    @Test void testGetUserById() { when(userRepository.getUserById(userId)).thenReturn(testUser); assertEquals(testUser, userService.getUserById(userId)); }
    @Test void testGetUserById_Null() { when(userRepository.getUserById(userId)).thenReturn(null); assertNull(userService.getUserById(userId)); }
    @Test void testGetUserById_VerifyCall() { userService.getUserById(userId); verify(userRepository, times(1)).getUserById(userId); }

    // getOrdersByUserId()
    @Test void testGetOrdersByUserId() { when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>(List.of(testOrder))); assertEquals(1, userService.getOrdersByUserId(userId).size()); }
    @Test void testGetOrdersByUserId_Empty() { when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>()); assertTrue(userService.getOrdersByUserId(userId).isEmpty()); }
    @Test void testGetOrdersByUserId_VerifyCall() { userService.getOrdersByUserId(userId); verify(userRepository, times(1)).getOrdersByUserId(userId); }

    // addOrderToUser()
    @Test void testAddOrderToUser() { when(cartRepository.getCartByUserId(userId)).thenReturn(testCart); userService.addOrderToUser(userId); verify(orderRepository, never()).addOrder(any()); }
    @Test void testAddOrderToUser_EmptyCart() { when(cartRepository.getCartByUserId(userId)).thenReturn(new Cart(UUID.randomUUID(), userId, new ArrayList<>())); userService.addOrderToUser(userId); verify(orderRepository, never()).addOrder(any()); }
    @Test void testAddOrderToUser_CartNull() { when(cartRepository.getCartByUserId(userId)).thenReturn(null); userService.addOrderToUser(userId); verify(orderRepository, never()).addOrder(any()); }

    // emptyCart()
    @Test void testEmptyCart() { when(cartRepository.getCartByUserId(userId)).thenReturn(testCart); userService.emptyCart(userId); assertTrue(testCart.getProducts().isEmpty()); }
    @Test void testEmptyCart_CartNull() { when(cartRepository.getCartByUserId(userId)).thenReturn(null); userService.emptyCart(userId); verify(cartRepository, never()).addCart(any()); }
    @Test void testEmptyCart_VerifyCall() { userService.emptyCart(userId); verify(cartRepository, times(1)).getCartByUserId(userId); }

    // removeOrderFromUser()
    @Test void testRemoveOrderFromUser() { doNothing().when(userRepository).removeOrderFromUser(userId, testOrder.getId()); userService.removeOrderFromUser(userId, testOrder.getId()); verify(userRepository).removeOrderFromUser(userId, testOrder.getId()); }
    @Test void testRemoveOrderFromUser_VerifyCall() { userService.removeOrderFromUser(userId, testOrder.getId()); verify(userRepository, times(1)).removeOrderFromUser(userId, testOrder.getId()); }

    // deleteUserById()
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteUserById(userId);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }
    @Test void testDeleteUserById_VerifyCall() { userService.deleteUserById(userId); verify(userRepository, times(1)).deleteUserById(userId); }
    @Test void testDeleteUserById_NeverCall() { userService.deleteUserById(userId); verify(userRepository, never()).deleteUserById(UUID.randomUUID()); }

    // userExists()
    @Test void testUserExists() { when(userRepository.getUserById(userId)).thenReturn(testUser); assertTrue(userService.userExists(userId)); }
    @Test void testUserExists_False() { when(userRepository.getUserById(userId)).thenReturn(null); assertFalse(userService.userExists(userId)); }
    @Test void testUserExists_VerifyCall() { userService.userExists(userId); verify(userRepository, times(1)).getUserById(userId); }
}


