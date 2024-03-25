package org.damiane.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.dto.trainer.TrainerProfileDTO;
import org.damiane.dto.trainer.TrainerRegistrationRequest;
import org.damiane.entity.*;
import org.damiane.mapper.TrainerMapper;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private AuthenticateServiceImpl authenticateService;

    @Mock
    private Trainer trainer;

    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerMapper trainerMapper;

    private TrainingType trainingType;
    private User user;

    @Test
    void createTrainer_CreatesTrainerSuccessfully_WhenValidDataProvided() {
        String firstName = "John";
        String lastName = "Doe";
        String specialization = "CARDIO";
        TrainerRegistrationRequest validRequest = new TrainerRegistrationRequest();
        validRequest.setFirstName(firstName);
        validRequest.setLastName(lastName);
        validRequest.setSpecialization(specialization);

        User user = new User();
        user.setUsername("John.Doe");
        user.setPassword("password");

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CARDIO);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        when(userService.createUser(firstName, lastName)).thenReturn(user);
        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(trainingType);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(validRequest);

        verify(userService).createUser(firstName, lastName);
        verify(trainingTypeRepository).save(any(TrainingType.class));
        verify(trainerRepository).save(any(Trainer.class));

        assertNotNull(createdTrainer);
        assertEquals(user, createdTrainer.getUser());
        assertEquals(trainingType, createdTrainer.getTrainingType());
    }


    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);

        trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingType(TrainingTypeValue.WEIGHT_TRAINING);

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
    }

    @Test
    public void testGetTrainerProfile() {
        String username = "testuser";
        String password = "testpassword";

        when(trainerRepository.findByUserUsername(username)).thenReturn(trainer);
        when(trainingRepository.findByTrainerId(trainer.getId())).thenReturn(Collections.emptyList());

        TrainerProfileDTO result = trainerService.getTrainerProfile(username, password);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue(result.isActive());
        assertEquals("WEIGHT_TRAINING", result.getSpecialization());
        assertTrue(result.getTrainees().isEmpty());

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findByUserUsername(username);
        verify(trainingRepository).findByTrainerId(trainer.getId());
    }

    @Test
    public void testGetTrainerProfile_TrainerNotFound() {
        String username = "unknownuser";
        String password = "testpassword";

        when(trainerRepository.findByUserUsername(username)).thenReturn(null);

        TrainerProfileDTO result = trainerService.getTrainerProfile(username, password);

        assertNull(result);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findByUserUsername(username);
        verify(trainingRepository, never()).findByTrainerId(anyLong());
    }


    @Test
    void testFindUnassignedActiveTrainersByTraineeUsername() {
        // Mocking trainee
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        // Mocking trainings
        List<Training> trainings = new ArrayList<>();
        // Assuming trainings are already assigned to the trainee
        // If you have specific scenarios, you can modify this accordingly

        // Mocking trainee repository
        when(traineeRepository.findByUserUsername("traineeUsername")).thenReturn(trainee);

        // Mocking training repository
        when(trainingRepository.findByTraineeId(1L)).thenReturn(trainings);

        // Mocking trainer repository
        List<Trainer> trainers = new ArrayList<>();
        // Assuming some trainers are unassigned
        // If you have specific scenarios, you can modify this accordingly
        when(trainerRepository.findAll()).thenReturn(trainers);

        // Call the method
        List<TrainerDTO> result = trainerService.findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");

        // Verify
        assertEquals(0, result.size()); // Assuming no unassigned trainers in this scenario
        verify(authenticateService, times(1)).matchUserCredentials("traineeUsername", "password");
        verify(traineeRepository, times(1)).findByUserUsername("traineeUsername");
        verify(trainingRepository, times(1)).findByTraineeId(1L);
        verify(trainerRepository, times(1)).findAll();
        // No need to verify conversion since it's not used in this scenario
    }
}
