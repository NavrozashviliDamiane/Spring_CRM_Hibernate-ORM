package org.damiane.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeTest {

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
    }

    @Test
    @DisplayName("Test setting and getting id")
    void testSettingAndGetId() {
        Long id = 1L;
        trainingType.setId(id);
        assertEquals(id, trainingType.getId());
    }

    @Test
    @DisplayName("Test setting and getting training type value")
    void testSettingAndGetTrainingTypeValue() {
        TrainingTypeValue trainingTypeValue = TrainingTypeValue.YOGA;
        trainingType.setTrainingType(trainingTypeValue);
        assertEquals(trainingTypeValue, trainingType.getTrainingType());
    }

    @Test
    @DisplayName("Test constructor with training type value")
    void testConstructorWithTrainingTypeValue() {
        TrainingTypeValue trainingTypeValue = TrainingTypeValue.CARDIO;
        TrainingType trainingType = new TrainingType(trainingTypeValue);
        assertEquals(trainingTypeValue, trainingType.getTrainingType());
    }
}
