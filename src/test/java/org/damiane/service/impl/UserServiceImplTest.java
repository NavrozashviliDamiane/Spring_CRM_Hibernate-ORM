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
    void changePassword_ChangesPasswordSuccessfully_WhenValidRequestProvided() {
        // Arrange
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

        // Act
        userService.changePassword(request);

        // Assert
        verify(authenticateService).matchUserCredentials(username, oldPassword);
        verify(userRepository).findByUsername(username);

        // Verify that the user's password was updated
        assertEquals(newPassword, user.getPassword());
        verify(userRepository).save(user);
    }

}
