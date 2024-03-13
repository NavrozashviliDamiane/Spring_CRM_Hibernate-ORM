package org.damiane.service.impl;

import org.damiane.entity.Trainee;
import org.damiane.entity.User;
import org.damiane.repository.TraineeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.damiane.service.TrainingService;
import org.damiane.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Given_ValidCredentials_When_GetAllTrainees_Then_ReturnListOfTrainees() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        List<Trainee> expectedTrainees = new ArrayList<>();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findAll()).thenReturn(expectedTrainees);

        // When
        List<Trainee> actualTrainees = traineeService.getAllTrainees(username, password);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findAll();
        assertEquals(expectedTrainees, actualTrainees);
    }


    @Test
    void Given_ValidCredentials_When_GetTraineeByUsername_Then_ReturnTrainee() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        Trainee expectedTrainee = new Trainee();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findByUserUsername(username)).thenReturn(expectedTrainee);

        // When
        Trainee actualTrainee = traineeService.getTraineeByUsername(username, password);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findByUserUsername(username);
        assertEquals(expectedTrainee, actualTrainee);
    }

    @Test
    void Given_ValidCredentialsAndNewPassword_When_ChangeTraineePassword_Then_PasswordIsChanged() {
        // Given
        Long traineeId = 1L;
        String username = "testUser";
        String password = "testPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findById(traineeId)).thenReturn(java.util.Optional.of(trainee));

        // When
        traineeService.changeTraineePassword(traineeId, username, password, newPassword);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findById(traineeId);
        assertEquals(newPassword, trainee.getUser().getPassword());
    }

    @Test
    void Given_TraineeData_When_CreateTrainee_Then_ReturnCreatedTrainee() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Date dateOfBirth = new Date();
        String address = "123 Street";
        Trainee createdTrainee = new Trainee();
        createdTrainee.setUser(new User());
        when(userService.createUser(firstName, lastName)).thenReturn(createdTrainee.getUser());
        when(traineeRepository.save(any())).thenReturn(createdTrainee);

        // When
        Trainee actualTrainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        // Then
        verify(userService).createUser(firstName, lastName);
        verify(traineeRepository).save(any());
        assertEquals(createdTrainee, actualTrainee);
    }

    @Test
    void Given_ValidCredentials_When_ActivateTrainee_Then_TraineeIsActive() {
        // Given
        Long traineeId = 1L;
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findById(traineeId)).thenReturn(java.util.Optional.of(trainee));

        // When
        traineeService.activateTrainee(traineeId, username, password);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findById(traineeId);
        assertTrue(trainee.getUser().isActive());
    }

    @Test
    void Given_ValidCredentials_When_DeactivateTrainee_Then_TraineeIsInactive() {
        // Given
        Long traineeId = 1L;
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findById(traineeId)).thenReturn(java.util.Optional.of(trainee));

        // When
        traineeService.deactivateTrainee(traineeId, username, password);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findById(traineeId);
        assertFalse(trainee.getUser().isActive());
    }

    @Test
    void Given_ValidCredentials_When_DeleteTraineeByUsername_Then_TraineeIsDeleted() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        // When
        traineeService.deleteTraineeByUsername(username, password);

        // Then
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainingService).updateTrainingForTrainee(username);
        verify(traineeRepository).findByUserUsername(username);
        verify(traineeRepository).delete(trainee);
    }


}
