package org.damiane.service.impl;

import org.damiane.entity.*;
import org.damiane.repository.*;
import org.damiane.service.AuthenticateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTrainings_ValidCredentials_ReturnListOfTrainings() {
        String username = "testUser";
        String password = "testPassword";
        List<Training> expectedTrainings = List.of(new Training(), new Training());

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainingRepository.findAll()).thenReturn(expectedTrainings);

        List<Training> actualTrainings = trainingService.getAllTrainings(username, password);

        assertEquals(expectedTrainings, actualTrainings);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainingRepository).findAll();
    }

    @Test
    void createTraining_ValidCredentials_ReturnCreatedTraining() {
        String traineeUsername = "trainee";
        String trainerUsername = "trainer";
        String trainingName = "Test Training";
        Date trainingDate = new Date();
        Integer trainingDuration = 60;
        String password = "password";

        Trainee trainee = new Trainee();
        trainee.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setId(2L);

        Training training = new Training();
        training.setId(1L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        when(authenticateService.matchUserCredentials(trainerUsername, password)).thenReturn(true);
        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);
        when(trainerRepository.findByUserUsername(trainerUsername)).thenReturn(trainer);
        when(trainingRepository.save(any())).thenReturn(training);

        Training createdTraining = trainingService.createTraining(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration, password);

        verify(authenticateService).matchUserCredentials(trainerUsername, password);
        verify(traineeRepository).findByUserUsername(traineeUsername);
        verify(trainerRepository).findByUserUsername(trainerUsername);
        verify(trainingRepository).save(any(Training.class));


    }

    @Test
    void deleteTraining_ValidCredentials_DeleteTrainingById() {
        Long trainingId = 1L;
        String username = "testUser";
        String password = "testPassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        trainingService.deleteTraining(trainingId, username, password);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainingRepository).deleteById(trainingId);
    }

    @Test
    void updateTrainingForTrainee_ExistingTrainee_UpdateTraining() {
        String traineeUsername = "trainee";

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);

        trainingService.updateTrainingForTrainee(traineeUsername);

        verify(traineeRepository).findByUserUsername(traineeUsername);
        verify(trainingRepository, times(1)).findByTraineeId(trainee.getId());
    }

    @Test
    void getTrainingsByTraineeUsernameAndCriteria_ValidCredentials_ReturnFilteredTrainingsForTrainee() {
        String username = "trainee";
        String password = "testPassword";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        TrainingTypeValue trainingTypeName = TrainingTypeValue.CARDIO;

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        Trainee trainee = new Trainee();
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        Trainer trainer = new Trainer();
        when(trainerRepository.findByUserUsername(trainerName)).thenReturn(trainer);

        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.findByTrainingType(trainingTypeName)).thenReturn(trainingType);

        List<Training> expectedTrainings = List.of(new Training(), new Training());
        when(trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                trainee.getId(), fromDate, toDate, trainer.getId(), trainingType.getId()))
                .thenReturn(expectedTrainings);

        List<Training> actualTrainings = trainingService.getTrainingsByTraineeUsernameAndCriteria(
                username, password, fromDate, toDate, trainerName, trainingTypeName);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findByUserUsername(username);
        verify(trainerRepository).findByUserUsername(trainerName);
        verify(trainingTypeRepository).findByTrainingType(trainingTypeName);
        verify(trainingRepository).findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                trainee.getId(), fromDate, toDate, trainer.getId(), trainingType.getId());

        assertEquals(expectedTrainings, actualTrainings);
    }

    @Test
    void getTrainingsByTrainerUsernameAndCriteria_ValidCredentials_ReturnFilteredTrainingsForTrainer() {
        String username = "trainer";
        String password = "testPassword";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        Trainer trainer = new Trainer();
        when(trainerRepository.findByUserUsername(username)).thenReturn(trainer);

        Trainee trainee = new Trainee();
        when(traineeRepository.findByUserUsername(traineeName)).thenReturn(trainee);

        List<Training> expectedTrainings = List.of(new Training(), new Training());
        when(trainingRepository.findByTrainerIdAndTrainingDateBetweenAndTraineeId(
                trainer.getId(), fromDate, toDate, trainee.getId()))
                .thenReturn(expectedTrainings);

        List<Training> actualTrainings = trainingService.getTrainingsByTrainerUsernameAndCriteria(
                username, password, fromDate, toDate, traineeName);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findByUserUsername(username);
        verify(traineeRepository).findByUserUsername(traineeName);
        verify(trainingRepository).findByTrainerIdAndTrainingDateBetweenAndTraineeId(
                trainer.getId(), fromDate, toDate, trainee.getId());

        assertEquals(expectedTrainings, actualTrainings);
    }
}
