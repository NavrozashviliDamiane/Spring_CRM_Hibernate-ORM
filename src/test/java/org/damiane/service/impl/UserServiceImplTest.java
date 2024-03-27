package org.damiane.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.damiane.dto.user.ChangePasswordRequest;
import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.damiane.service.AuthenticateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void changePassword_SuccessfullyChangesPassword_WhenValidRequestProvided() {
        String username = "john";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUsername(username);
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);

        User user = new User();
        user.setUsername(username);
        user.setPassword(oldPassword);

        when(authenticateService.matchUserCredentials(username, oldPassword)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(user);

        userService.changePassword(request);

        verify(authenticateService).matchUserCredentials(username, oldPassword);
        verify(userRepository).findByUsername(username);

        assertEquals(newPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_CreatesUserSuccessfully_WhenValidParametersProvided() {
        String firstName = "John";
        String lastName = "Doe";

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setActive(true);

        when(userRepository.save(any())).thenReturn(user);

        User createdUser = userService.createUser(firstName, lastName);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUserById_DeletesUserByIdSuccessfully() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void saveUser_SavesUserSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.save(any())).thenReturn(user);

        User savedUser = userService.saveUser(user);

        verify(userRepository).save(any(User.class));
    }

}
