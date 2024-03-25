package org.damiane.mapper;

import org.damiane.dto.trainer.TrainerTrainingResponseDTO;
import org.damiane.entity.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerTrainingMapperTest {

    @Test
    void mapTrainingToResponseDTO_ReturnsCorrectDTO() {
        User user = new User();
        user.setUsername("trainee_username");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingType(TrainingTypeValue.CARDIO);

        Training training = new Training();
        training.setTrainingName("Training Name");
        training.setTrainingDate(new Date());
        training.setTrainingType(trainingType);
        training.setTrainingDuration(60);
        training.setTrainee(trainee);

        TrainerTrainingMapper mapper = new TrainerTrainingMapper();

        TrainerTrainingResponseDTO responseDTO = mapper.mapTrainingToResponseDTO(training);

        assertEquals("Training Name", responseDTO.getTrainingName());
        assertEquals(training.getTrainingDate(), responseDTO.getTrainingDate());
        assertEquals("CARDIO", responseDTO.getTrainingType());
        assertEquals(60, responseDTO.getTrainingDuration());
        assertEquals("trainee_username", responseDTO.getTraineeName());
    }
}
