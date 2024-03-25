package org.damiane.controller;

import org.apache.logging.log4j.Logger;
import org.damiane.dto.user.UserCredentialsDTO;
import org.damiane.dto.trainee.TraineeRegistrationDTO;
import org.damiane.entity.Trainee;
import org.damiane.entity.User;
import org.damiane.mapper.TraineeMapper;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private Logger logger;

    @Mock
    private TraineeMapper traineeMapper;


    @Mock
    private AuthenticateService authenticateService;


    @InjectMocks
    private TraineeController traineeController;

    @Test
    void registerTrainee_CreatesTraineeSuccessfully_WhenValidRegistrationDataProvided() {
        TraineeRegistrationDTO registrationDTO = new TraineeRegistrationDTO();
        registrationDTO.setFirstName("Damiane");
        registrationDTO.setLastName("Navrozashvili");
        registrationDTO.setDateOfBirth(new Date());
        registrationDTO.setAddress("Tbilisi");

        User user = new User();
        user.setUsername("Damiane.Navrozashvili");
        user.setPassword("password");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeService.createTrainee(anyString(), anyString(), any(Date.class), anyString())).thenReturn(trainee);

        ResponseEntity<UserCredentialsDTO> responseEntity = traineeController.registerTrainee(registrationDTO);

        verify(traineeService).createTrainee(anyString(), anyString(), any(Date.class), anyString());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(user.getUsername(), responseEntity.getBody().getUsername());
        assertEquals(user.getPassword(), responseEntity.getBody().getPassword());
    }


    @Test
    void deleteTraineeProfile_Successful() {
        // Mocking data
        String username = "testUser";
        String password = "password";

        // Stubbing authenticateService
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        // Invoke the method
        ResponseEntity<String> responseEntity = traineeController.deleteTraineeProfile(username, password);

        // Verify interactions and assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee profile deleted successfully", responseEntity.getBody());
        verify(traineeService).deleteTraineeByUsername(username);
        verify(authenticateService).matchUserCredentials(username, password);
    }

    @Test
    void deleteTraineeProfile_Unauthorized() {
        // Mocking data
        String username = "testUser";
        String password = "password";

        // Stubbing authenticateService
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        // Invoke the method
        ResponseEntity<String> responseEntity = traineeController.deleteTraineeProfile(username, password);

        // Verify interactions and assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
        verifyNoInteractions(traineeService);
        verify(authenticateService).matchUserCredentials(username, password);
    }



}
