package org.damiane.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Test setting and getting id")
    void testSettingAndGetId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    @DisplayName("Test setting and getting first name")
    void testSettingAndGetFirstName() {
        String firstName = "John";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    @DisplayName("Test setting and getting last name")
    void testSettingAndGetLastName() {
        String lastName = "Doe";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    @DisplayName("Test setting and getting username")
    void testSettingAndGetUsername() {
        String username = "johndoe";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    @DisplayName("Test setting and getting password")
    void testSettingAndGetPassword() {
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Test setting and getting active status")
    void testSettingAndGetActiveStatus() {
        boolean isActive = true;
        user.setActive(isActive);
        assertEquals(isActive, user.isActive());
    }
}
