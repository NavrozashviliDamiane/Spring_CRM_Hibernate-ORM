package org.damiane.mapper;

import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerMapperTest {

    @Test
    void convertToTrainerDTO_ReturnsCorrectTrainerDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CARDIO);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        TrainerMapper trainerMapper = new TrainerMapper();

        TrainerDTO trainerDTO = trainerMapper.convertToTrainerDTO(trainer);

        assertEquals("username", trainerDTO.getUsername());
        assertEquals("John", trainerDTO.getFirstName());
        assertEquals("Doe", trainerDTO.getLastName());
        assertEquals("CARDIO", trainerDTO.getSpecialization());
    }
}
