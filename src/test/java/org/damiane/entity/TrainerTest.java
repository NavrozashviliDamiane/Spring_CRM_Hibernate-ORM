package org.damiane.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerTest {

    @Mock
    private User mockUser;

    @Mock
    private TrainingType mockTrainingType;

    @InjectMocks
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        trainer = new Trainer();
    }

    @Test
    @DisplayName("Test setting and getting id")
    void testSettingAndGetId() {
        Long id = 1L;
        trainer.setId(id);
        assertEquals(id, trainer.getId());
    }

    @Test
    @DisplayName("Test setting and getting user")
    void testSettingAndGetUser() {
        trainer.setUser(mockUser);
        assertEquals(mockUser, trainer.getUser());
    }

    @Test
    @DisplayName("Test setting and getting training type")
    void testSettingAndGetTrainingType() {
        trainer.setTrainingType(mockTrainingType);
        assertEquals(mockTrainingType, trainer.getTrainingType());
    }
}


