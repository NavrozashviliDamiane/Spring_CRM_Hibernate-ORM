package org.damiane.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.damiane.dto.trainer.*;
import org.damiane.entity.*;
import org.damiane.mapper.TrainerMapper;
import org.damiane.mapper.TrainerTrainingMapper;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.UserService;
import org.damiane.util.trainer.TrainerProfileDtoCreator;
import org.damiane.util.trainer.TrainerSpecializationUpdater;
import org.damiane.util.user.UserUpdateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private TrainerProfileDtoCreator profileDtoCreator;

    @Mock
    private TrainerTrainingMapper trainerTrainingMapper;

    @Mock
    private TrainerSpecializationUpdater specializationUpdater;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserUpdateHelper userUpdateHelper;

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
    void When_CreateTrainerWithValidData_Then_TrainerIsSuccessfullyCreated() {
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
    public void When_GetTrainerProfileWithExistingUser_Then_ReturnTrainerProfileDTO() {
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
    public void When_GetTrainerProfileWithNonExistingUser_Then_ReturnNull() {
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
    void When_UpdateTrainerProfileWithExistingUser_Then_SpecializationIsUpdated() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        List<Training> trainings = new ArrayList<>();

        when(traineeRepository.findByUserUsername("traineeUsername")).thenReturn(trainee);

        when(trainingRepository.findByTraineeId(1L)).thenReturn(trainings);

        List<Trainer> trainers = new ArrayList<>();
        when(trainerRepository.findAll()).thenReturn(trainers);

        List<TrainerDTO> result = trainerService.findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");

        assertEquals(0, result.size());
        verify(authenticateService, times(1)).matchUserCredentials("traineeUsername", "password");
        verify(traineeRepository, times(1)).findByUserUsername("traineeUsername");
        verify(trainingRepository, times(1)).findByTraineeId(1L);
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void When_FindUnassignedActiveTrainersByTraineeUsername_Then_ReturnTrainersList() {
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        trainerUpdateDTO.setUsername("username");
        trainerUpdateDTO.setPassword("password");
        trainerUpdateDTO.setSpecialization("someSpecialization"); // Assuming this is set properly

        Trainer trainer = new Trainer();
        when(trainerRepository.findByUserUsername(trainerUpdateDTO.getUsername())).thenReturn(trainer);
        when(authenticateService.matchUserCredentials(trainerUpdateDTO.getUsername(), trainerUpdateDTO.getPassword())).thenReturn(true);

        trainerService.updateTrainerProfile(trainerUpdateDTO);

        verify(specializationUpdater).updateSpecialization(trainer, trainerUpdateDTO);
    }


    @Test
    void When_GetTrainerTrainings_WithValidRequest_Then_ReturnTrainerTrainingsList() {
        TrainerTrainingsRequestDTO request = new TrainerTrainingsRequestDTO();
        request.setUsername("username");
        LocalDate periodFrom = LocalDate.of(2024, 1, 1);
        LocalDate periodTo = LocalDate.of(2024, 12, 31);
        request.setTraineeName("traineeName");

        List<Training> trainings = Collections.singletonList(new Training());
        when(trainingRepository.findByTrainerUserUsernameAndTrainingDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
                request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTraineeName()))
                .thenReturn(trainings);

        List<TrainerTrainingResponseDTO> expectedResponse = Collections.singletonList(null);

        when(trainerTrainingMapper.mapTrainingToResponseDTO(ArgumentMatchers.any()))
                .thenReturn(null);

        List<TrainerTrainingResponseDTO> actualResponse = trainerService.getTrainerTrainings(request);

        assertEquals(expectedResponse, actualResponse);
        verify(trainingRepository).findByTrainerUserUsernameAndTrainingDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
                request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTraineeName());
        verify(trainerTrainingMapper, times(trainings.size())).mapTrainingToResponseDTO(ArgumentMatchers.any());
    }


    @Test
    void When_UpdateTrainerStatus_Then_TrainerStatusIsUpdated() {
        String username = "username";
        boolean isActive = false;

        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername(username)).thenReturn(trainer);

        trainerService.updateTrainerStatus(username, isActive);

        verify(userService).saveUser(user);
        assertEquals(isActive, user.isActive());
    }

}
