package org.damiane.util.trainer;


import org.damiane.dto.trainer.TrainerProfileDTO;
import org.damiane.entity.*;
import org.damiane.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerProfileDtoCreatorTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainerProfileDtoCreator trainerProfileDtoCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTrainerProfileDTO_CreatesProfileDTO_WhenTrainerAndTrainingsExist() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingType(TrainingTypeValue.YOGA);
        trainer.setTrainingType(trainingType);
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        Trainee trainee = new Trainee();
        User traineeUser = new User();
        traineeUser.setUsername("trainee_username");
        traineeUser.setFirstName("Trainee");
        traineeUser.setLastName("User");
        trainee.setUser(traineeUser);
        training.setTrainee(trainee);
        trainings.add(training);

        when(trainingRepository.findByTrainerId(null)).thenReturn(trainings);

        TrainerProfileDTO profileDTO = trainerProfileDtoCreator.createTrainerProfileDTO(trainer);

        assertEquals("John", profileDTO.getFirstName());
        assertEquals("Doe", profileDTO.getLastName());
        assertEquals(true, profileDTO.isActive());
        assertEquals("YOGA", profileDTO.getSpecialization());
        assertEquals(1, profileDTO.getTrainees().size());
        assertEquals("trainee_username", profileDTO.getTrainees().get(0).getUsername());
        assertEquals("Trainee", profileDTO.getTrainees().get(0).getFirstName());
        assertEquals("User", profileDTO.getTrainees().get(0).getLastName());
    }
}
