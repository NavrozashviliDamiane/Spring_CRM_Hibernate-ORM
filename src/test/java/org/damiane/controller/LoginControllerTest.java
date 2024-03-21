package org.damiane.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.damiane.service.AuthenticateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private LoginController loginController;

    @Test
    void login_ReturnsOkResponse_WhenCredentialsMatch() {
        String username = "john";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        ResponseEntity<String> responseEntity = loginController.login(username, password);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login successful", responseEntity.getBody());
    }

    @Test
    void login_ReturnsUnauthorizedResponse_WhenCredentialsDoNotMatch() {
        // Arrange
        String username = "john";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = loginController.login(username, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
    }
}
