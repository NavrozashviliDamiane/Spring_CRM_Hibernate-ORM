package org.damiane.service.impl;


import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.entity.User;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "testUser";
        String password = "testPassword";
        Trainer expectedTrainer = new Trainer();

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerRepository.findByUserUsername(username)).thenReturn(expectedTrainer);

        Trainer actualTrainer = trainerService.getTrainerByUsername(username, password);

        assertEquals(expectedTrainer, actualTrainer);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findByUserUsername(username);
    }

    @Test
    void testChangeTrainerPassword() {
        Long trainerId = 1L;
        String username = "testUser";
        String password = "testPassword";
        String newPassword = "newTestPassword";

        Trainer trainer = new Trainer();
        User user = new User();
        user.setPassword(password); // Set the password for the user
        trainer.setUser(user); // Set the user in the trainer object

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        trainerService.changeTrainerPassword(trainerId, username, password, newPassword);

        assertEquals(newPassword, trainer.getUser().getPassword());
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findById(trainerId);
        verify(userService).saveUser(any()); // Ensure userService.saveUser() is called
    }

    @Test
    void testGetTrainerById() {
        Long trainerId = 1L;
        String username = "testUser";
        String password = "testPassword";
        Trainer expectedTrainer = new Trainer();

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(expectedTrainer));

        Trainer actualTrainer = trainerService.getTrainerById(trainerId, username, password);

        assertEquals(expectedTrainer, actualTrainer);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findById(trainerId);
    }

    @Test
    void testUpdateTrainerProfile() {
        String username = "testUser";
        String password = "testPassword";
        String firstName = "John";
        String lastName = "Doe";
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        Trainer trainer  = new Trainer();
        User user = new User();
        TrainingType existingTrainingType = new TrainingType();
        existingTrainingType.setId(1L);
        trainer.setUser(user);

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerRepository.findByUserUsername(username)).thenReturn(trainer);
        when(trainingTypeRepository.findByTrainingType(trainingType.getTrainingType())).thenReturn(existingTrainingType);

        Trainer updatedTrainer = trainerService.updateTrainerProfile(username, password, firstName, lastName, TrainingTypeValue.CARDIO);

        assertEquals(firstName, updatedTrainer.getUser().getFirstName());
        assertEquals(lastName, updatedTrainer.getUser().getLastName());
        assertEquals(existingTrainingType, updatedTrainer.getTrainingType());
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findByUserUsername(username);
        verify(trainingTypeRepository).findByTrainingType(trainingType.getTrainingType());
        verify(userService).saveUser(trainer.getUser());
    }
}
