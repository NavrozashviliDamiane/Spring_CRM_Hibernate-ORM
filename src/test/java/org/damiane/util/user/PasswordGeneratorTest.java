package org.damiane.util.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

    @Test
    public void testGeneratePassword() {
        int length = 10;
        String generatedPassword = PasswordGenerator.generatePassword(length);
        assertEquals(length, generatedPassword.length());

        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (char c : generatedPassword.toCharArray()) {
            assertTrue(validCharacters.contains(String.valueOf(c)));
        }

        int[] lengths = {8, 12, 16};
        for (int len : lengths) {
            String password = PasswordGenerator.generatePassword(len);
            assertEquals(len, password.length());
        }
    }
}
