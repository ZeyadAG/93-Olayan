package com.yourpackage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yourpackage.model.User;
import com.yourpackage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mocking the repository

    @InjectMocks
    private UserService userService; // Injecting the mock into UserService

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User(userId, "John Doe", "johndoe@example.com"); 
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.addUser(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testGetUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getUsers();

        assertEquals(1, users.size());
        assertEquals(testUser.getId(), users.get(0).getId());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        User retrievedUser = userService.getUserById(userId);

        assertNotNull(retrievedUser);
        assertEquals(testUser.getId(), retrievedUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }
}
