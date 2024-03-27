package org.damiane.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class TrainingTest {

    @Mock
    private Trainee mockTrainee;

    @Mock
    private Trainer mockTrainer;

    @Mock
    private TrainingType mockTrainingType;

    @InjectMocks
    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        training = new Training();
    }

    @Test
    @DisplayName("Test setting and getting id")
    void testSettingAndGetId() {
        Long id = 1L;
        training.setId(id);
        assertEquals(id, training.getId());
    }

    @Test
    @DisplayName("Test setting and getting trainee")
    void testSettingAndGetTrainee() {
        training.setTrainee(mockTrainee);
        assertEquals(mockTrainee, training.getTrainee());
    }

    @Test
    @DisplayName("Test setting and getting trainer")
    void testSettingAndGetTrainer() {
        training.setTrainer(mockTrainer);
        assertEquals(mockTrainer, training.getTrainer());
    }

    @Test
    @DisplayName("Test setting and getting training type")
    void testSettingAndGetTrainingType() {
        training.setTrainingType(mockTrainingType);
        assertEquals(mockTrainingType, training.getTrainingType());
    }

    @Test
    @DisplayName("Test setting and getting training name")
    void testSettingAndGetTrainingName() {
        String trainingName = "Test Training";
        training.setTrainingName(trainingName);
        assertEquals(trainingName, training.getTrainingName());
    }


    @Test
    @DisplayName("Test setting and getting training duration")
    void testSettingAndGetTrainingDuration() {
        int trainingDuration = 60;
        training.setTrainingDuration(trainingDuration);
        assertEquals(trainingDuration, training.getTrainingDuration());
    }
}
