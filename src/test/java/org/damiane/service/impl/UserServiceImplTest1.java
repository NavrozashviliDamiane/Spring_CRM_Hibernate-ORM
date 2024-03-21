//package org.damiane.service.impl;
//
//import org.damiane.entity.User;
//import org.damiane.repository.UserRepository;
//import org.damiane.service.AuthenticateService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class UserServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthenticateService authenticateService;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void Given_ValidCredentials_When_GetAllUsers_Then_ReturnListOfUsers() {
//        String username = "testUser";
//        String password = "testPassword";
//        List<User> expectedUsers = new ArrayList<>();
//
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(userRepository.findAll()).thenReturn(expectedUsers);
//
//        List<User> actualUsers = userService.getAllUsers(username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(userRepository).findAll();
//
//        assertEquals(expectedUsers, actualUsers);
//    }
//
//    @Test
//    void Given_ValidCredentials_When_GetUserById_Then_ReturnUser() {
//        Long userId = 1L;
//        String username = "testUser";
//        String password = "testPassword";
//        User expectedUser = new User();
//
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//
//        User actualUser = userService.getUserById(userId, username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(userRepository).findById(userId);
//
//        assertEquals(expectedUser, actualUser);
//    }
//
//    @Test
//    void Given_UserData_When_CreateUser_Then_ReturnCreatedUser() {
//        String firstName = "John";
//        String lastName = "Doe";
//        User createdUser = new User();
//
//        when(userRepository.save(any())).thenReturn(createdUser);
//
//        User actualUser = userService.createUser(firstName, lastName);
//
//        verify(userRepository).save(any());
//
//        assertEquals(createdUser, actualUser);
//    }
//
//    @Test
//    void Given_ValidCredentials_When_DeleteUser_Then_DeleteUserById() {
//        Long userId = 1L;
//        String username = "testUser";
//        String password = "testPassword";
//
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//
//        userService.deleteUser(userId, username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(userRepository).deleteById(userId);
//    }
//
//    @Test
//    void Given_User_When_SaveUser_Then_SaveUserToRepository() {
//        User user = new User();
//
//        when(userRepository.save(user)).thenReturn(user);
//
//        User savedUser = userService.saveUser(user);
//
//        verify(userRepository).save(user);
//
//        assertEquals(user, savedUser);
//    }
//}
