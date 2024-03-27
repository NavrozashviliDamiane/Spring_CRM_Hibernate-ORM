package org.damiane.mapper;

import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.entity.*;
import org.damiane.mapper.TrainingToTrainerMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingToTrainerMapperTest {

    private final TrainingToTrainerMapper mapper = new TrainingToTrainerMapper();

    @Test
    void mapTrainingToTrainerDTO_TrainingWithTrainerAndType_ReturnsCorrectTrainerDTO() {
        Training training = mock(Training.class);
        Trainer trainer = new Trainer();
        User trainerUser = new User();
        trainerUser.setUsername("username");
        trainerUser.setFirstName("John");
        trainerUser.setLastName("Doe");
        trainer.setUser(trainerUser);

        TrainingType trainingType = mock(TrainingType.class);
        when(trainingType.getTrainingType()).thenReturn(TrainingTypeValue.CARDIO);

        when(training.getTrainer()).thenReturn(trainer);
        when(training.getTrainingType()).thenReturn(trainingType);

        TrainerDTO trainerDTO = mapper.mapTrainingToTrainerDTO(training);

        assertEquals("username", trainerDTO.getUsername());
        assertEquals("John", trainerDTO.getFirstName());
        assertEquals("Doe", trainerDTO.getLastName());
        assertEquals("CARDIO", trainerDTO.getSpecialization());
    }
}
