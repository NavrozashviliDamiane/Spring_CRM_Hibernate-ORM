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
    void Given_AuthenticateServiceInitialized_When_MatchingUserCredentials_Then_ReturnsOkResponseWhenCredentialsMatch() {
        String username = "john";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        ResponseEntity<String> responseEntity = loginController.login(username, password);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login successful", responseEntity.getBody());
    }

    @Test
    void Given_AuthenticateServiceInitialized_When_MatchingUserCredentials_Then_ReturnsUnauthorizedResponseWhenCredentialsDoNotMatch() {
        String username = "john";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<String> responseEntity = loginController.login(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
    }
}
