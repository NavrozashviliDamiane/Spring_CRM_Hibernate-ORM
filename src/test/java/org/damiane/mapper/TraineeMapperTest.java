package org.damiane.mapper;

import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.entity.Trainee;
import org.damiane.entity.Trainer;
import org.damiane.entity.Training;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.entity.User;
import org.damiane.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TraineeMapperTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeMapper traineeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void GivenTraineeWithTrainingData_WhenMappingToDTO_ThenReturnCorrectTraineeProfileDTO() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setAddress("123 Main St");
        trainee.setDateOfBirth(new Date());

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        trainee.setUser(user);

        TrainingTypeValue type1 = TrainingTypeValue.CARDIO;
        TrainingTypeValue type2 = TrainingTypeValue.YOGA;

        Training training1 = new Training();
        training1.setId(1L);
        training1.setTrainingType(new TrainingType(type1));

        Trainer trainer1 = new Trainer();
        User trainerUser1 = new User();
        trainerUser1.setUsername("trainer1");
        trainerUser1.setFirstName("Trainer");
        trainerUser1.setLastName("One");
        trainer1.setUser(trainerUser1);
        training1.setTrainer(trainer1);

        Training training2 = new Training();
        training2.setId(2L);
        training2.setTrainingType(new TrainingType(type2));

        Trainer trainer2 = new Trainer();
        User trainerUser2 = new User();
        trainerUser2.setUsername("trainer2");
        trainerUser2.setFirstName("Trainer");
        trainerUser2.setLastName("Two");
        trainer2.setUser(trainerUser2);
        training2.setTrainer(trainer2);

        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);

        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        TraineeProfileDTO profileDTO = traineeMapper.mapTraineeToDTO(trainee);

        assertEquals("John", profileDTO.getFirstName());
        assertEquals("Doe", profileDTO.getLastName());
        assertEquals("123 Main St", profileDTO.getAddress());
        assertEquals(true, profileDTO.isActive());

        List<TrainerDTO> trainers = profileDTO.getTrainers();
        assertEquals(2, trainers.size());

        TrainerDTO trainerDTO1 = trainers.get(0);
        assertEquals("trainer1", trainerDTO1.getUsername());
        assertEquals("Trainer", trainerDTO1.getFirstName());
        assertEquals("One", trainerDTO1.getLastName());
        assertEquals("CARDIO", trainerDTO1.getSpecialization());

        TrainerDTO trainerDTO2 = trainers.get(1);
        assertEquals("trainer2", trainerDTO2.getUsername());
        assertEquals("Trainer", trainerDTO2.getFirstName());
        assertEquals("Two", trainerDTO2.getLastName());
        assertEquals("YOGA", trainerDTO2.getSpecialization());
    }
}
