//package org.damiane.util;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PasswordGeneratorTest {
//
//    @Test
//    void testGeneratePassword() {
//        int length = 10;
//        String generatedPassword = PasswordGenerator.generatePassword(length);
//
//        assertEquals(length, generatedPassword.length());
//
//        assertFalse(generatedPassword.matches("[^A-Za-z0-9]"));
//
//        assertTrue(generatedPassword.matches(".*[a-z].*"));
//
//        assertTrue(generatedPassword.matches(".*[A-Z].*"));
//
//        assertTrue(generatedPassword.matches(".*[0-9].*"));
//    }
//}
