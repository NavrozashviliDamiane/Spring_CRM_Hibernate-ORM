package org.damiane.service.impl;

import org.damiane.entity.User;
import org.damiane.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticateServiceImpl authenticateService;

    @Test
    public void Given_ValidCredentials_When_MatchUserCredentials_Then_ReturnTrue() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(user);

        boolean result = authenticateService.matchUserCredentials(username, password);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void Given_InvalidUsername_When_MatchUserCredentials_Then_ReturnFalse() {
        String username = "nonExistingUser";
        String password = "testPassword";
        when(userRepository.findByUsername(username)).thenReturn(null);

        boolean result = authenticateService.matchUserCredentials(username, password);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
    }
}
