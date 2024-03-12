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
    void testGetAllTrainings() {
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
    void testCreateTraining() {
        // Prepare test data
        String traineeUsername = "trainee";
        String trainerUsername = "trainer";
        TrainingTypeValue trainingTypeValue = TrainingTypeValue.CARDIO;
        String trainingName = "Test Training";
        Date trainingDate = new Date();
        Integer trainingDuration = 60;
        String password = "testPassword";

        when(authenticateService.matchUserCredentials(trainerUsername, password)).thenReturn(true);

        Trainee trainee = new Trainee();
        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);

        Trainer trainer = new Trainer();
        when(trainerRepository.findByUserUsername(trainerUsername)).thenReturn(trainer);

        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.findByTrainingType(trainingTypeValue)).thenReturn(trainingType);

        Training createdTraining = new Training();
        when(trainingRepository.save(any(Training.class))).thenReturn(createdTraining);

        Training actualTraining = trainingService.createTraining(
                traineeUsername, trainerUsername, trainingTypeValue,
                trainingName, trainingDate, trainingDuration, password);

        verify(authenticateService).matchUserCredentials(trainerUsername, password);
        verify(traineeRepository).findByUserUsername(traineeUsername);
        verify(trainerRepository).findByUserUsername(trainerUsername);
        verify(trainingTypeRepository).findByTrainingType(trainingTypeValue);
        verify(trainingRepository).save(any(Training.class));

        assertEquals(createdTraining, actualTraining);
    }

    @Test
    void testDeleteTraining() {
        Long trainingId = 1L;
        String username = "testUser";
        String password = "testPassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        trainingService.deleteTraining(trainingId, username, password);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainingRepository).deleteById(trainingId);
    }

    @Test
    void testUpdateTrainingForTrainee() {
        String traineeUsername = "trainee";

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);

        trainingService.updateTrainingForTrainee(traineeUsername);

        verify(traineeRepository).findByUserUsername(traineeUsername);
        verify(trainingRepository, times(1)).findByTraineeId(trainee.getId());
    }

    @Test
    void testGetTrainingsByTraineeUsernameAndCriteria() {
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
    void testGetTrainingsByTrainerUsernameAndCriteria() {
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
