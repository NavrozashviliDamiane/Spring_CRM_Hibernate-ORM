package org.damiane.util;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UsernameGeneratorTest {

    @Test
    void testGenerateUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        UsernameGenerator usernameGenerator = new UsernameGenerator(userRepository);

        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";
        String generatedUsername = usernameGenerator.generateUsername(firstName, lastName);
        assertEquals(expectedUsername, generatedUsername);

        when(userRepository.existsByUsername(generatedUsername)).thenReturn(true);
        generatedUsername = usernameGenerator.generateUsername(firstName, lastName);
        assertNotEquals(expectedUsername, generatedUsername);
    }
}
