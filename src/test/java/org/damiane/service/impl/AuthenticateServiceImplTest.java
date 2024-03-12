package org.damiane.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.entity.User;
import org.damiane.exception.UnauthorizedAccessException;
import org.damiane.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceImplTest {

    @InjectMocks
    private AuthenticateServiceImpl authenticateService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "John", "Doe", "john.doe", "password", true);
    }

    @Test
    public void matchUserCredentials_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        boolean result = authenticateService.matchUserCredentials("john.doe", "password");

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername("john.doe");
    }

    @Test
    public void matchUserCredentials_failure() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UnauthorizedAccessException.class, () -> authenticateService.matchUserCredentials("john.doe", "password"));
        verify(userRepository, times(1)).findByUsername("john.doe");
    }

    @Test
    public void matchUserCredentials_failure_wrongPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        assertThrows(UnauthorizedAccessException.class, () -> authenticateService.matchUserCredentials("john.doe", "wrong_password"));
        verify(userRepository, times(1)).findByUsername("john.doe");
    }
}