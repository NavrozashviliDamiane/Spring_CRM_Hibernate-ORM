package org.damiane.service.impl;

import org.damiane.entity.Trainee;
import org.damiane.exception.UnauthorizedAccessException;
import org.damiane.repository.TraineeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private UnauthorizedAccessException unauthorizedAccessException;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTrainees() {
        String username = "testUser";
        String password = "testPassword";
        List<Trainee> expectedTrainees = Collections.singletonList(new Trainee());

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findAll()).thenReturn(expectedTrainees);

        List<Trainee> actualTrainees = traineeService.getAllTrainees(username, password);

        assertEquals(expectedTrainees, actualTrainees);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findAll();
    }

    @Test
    void testGetTraineeById() {
        Long traineeId = 1L;
        String username = "testUser";
        String password = "testPassword";
        Trainee expectedTrainee = new Trainee();

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(expectedTrainee));

        Trainee actualTrainee = traineeService.getTraineeById(traineeId, username, password);

        assertEquals(expectedTrainee, actualTrainee);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findById(traineeId);
    }

    @Test
    void testGetTraineeById_NotFound() {
        Long traineeId = 1L;
        String username = "testUser";
        String password = "testPassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        Trainee actualTrainee = traineeService.getTraineeById(traineeId, username, password);

        assertNull(actualTrainee);
        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeRepository).findById(traineeId);
    }

    @Test
    void testGetAllTrainees_AuthenticationFailure() {
        String username = "testUser";
        String password = "testPassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

    }

}