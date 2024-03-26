package org.damiane.controller;

import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainee.TraineeUpdateDTO;
import org.damiane.dto.trainer.TrainerResponse;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.dto.user.UserCredentialsDTO;
import org.damiane.dto.trainee.TraineeRegistrationDTO;
import org.damiane.entity.Trainee;
import org.damiane.entity.User;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private AuthenticateService authenticateService;


    @InjectMocks
    private TraineeController traineeController;

    @Test
    void Given_TraineeServiceInitialized_When_RegisteringTrainee_Then_ReturnsCreatedResponseOnValidRegistrationData() {
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
    void Given_AuthenticateServiceInitialized_When_DeletingTraineeProfile_Then_ReturnsOkResponseOnSuccessfulDeletion() {
        String username = "testUser";
        String password = "password";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        ResponseEntity<String> responseEntity = traineeController.deleteTraineeProfile(username, password);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee profile deleted successfully", responseEntity.getBody());
        verify(traineeService).deleteTraineeByUsername(username);
        verify(authenticateService).matchUserCredentials(username, password);
    }

    @Test
    void Given_AuthenticateServiceInitialized_When_DeletingTraineeProfile_Then_ReturnsUnauthorizedResponseOnInvalidCredentials() {
        String username = "testUser";
        String password = "password";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<String> responseEntity = traineeController.deleteTraineeProfile(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
        verifyNoInteractions(traineeService);
        verify(authenticateService).matchUserCredentials(username, password);
    }

    @Test
    public void Given_TraineeServiceInitialized_When_UpdatingTraineeTrainerList_Then_ReturnsOkResponseOnSuccess() {
        List<TrainerResponse> updatedTrainers = Arrays.asList(
                new TrainerResponse("username1", "John", "Doe", "Specialization1"),
                new TrainerResponse("username2", "Jane", "Smith", "Specialization2")
        );
        when(traineeService.updateTraineeTrainerList(anyString(), anyList())).thenReturn(updatedTrainers);

        ResponseEntity<List<TrainerResponse>> responseEntity = traineeController.updateTraineeTrainerList("traineeUsername", Arrays.asList("trainerUsername1", "trainerUsername2"));

        verify(traineeService).updateTraineeTrainerList("traineeUsername", Arrays.asList("trainerUsername1", "trainerUsername2"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTrainers, responseEntity.getBody());
    }

    @Test
    public void Given_TraineeServiceInitialized_When_UpdatingTraineeTrainerList_Then_ReturnsNotFoundResponseIfTraineeNotFound() {
        when(traineeService.updateTraineeTrainerList(anyString(), anyList())).thenReturn(null);

        ResponseEntity<List<TrainerResponse>> responseEntity = traineeController.updateTraineeTrainerList("nonExistingTrainee", Arrays.asList("trainerUsername1", "trainerUsername2"));

        verify(traineeService).updateTraineeTrainerList("nonExistingTrainee", Arrays.asList("trainerUsername1", "trainerUsername2"));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void Given_AuthenticateServiceInitialized_When_GettingTraineeProfile_Then_ReturnsOkResponseOnValidCredentials() {
        String username = "test";
        String password = "password";
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeService.getTraineeProfile(username)).thenReturn(profileDTO);

        ResponseEntity<?> response = traineeController.getTraineeProfile(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getTraineeProfile_Authenticated_Success() {
        String username = "test";
        String password = "password";
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeService.getTraineeProfile(username)).thenReturn(profileDTO);

        ResponseEntity<?> response = traineeController.getTraineeProfile(username, password);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(traineeService).getTraineeProfile(username);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertSame(profileDTO, response.getBody());
    }

    @Test
    public void Given_AuthenticateServiceInitialized_When_GettingTraineeProfile_Then_ReturnsUnauthorizedResponseOnInvalidCredentials() {
        String username = "test";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<?> response = traineeController.getTraineeProfile(username, password);

        verify(authenticateService).matchUserCredentials(username, password);
        assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    public void Given_AuthenticateServiceInitialized_When_GettingTraineeProfile_Then_ReturnsInternalServerErrorOnServerError() {
        String username = "test";
        String password = "password";
        when(authenticateService.matchUserCredentials(username, password)).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = traineeController.getTraineeProfile(username, password);

        verify(authenticateService).matchUserCredentials(username, password);
        assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void Given_AuthenticateServiceInitialized_When_UpdatingTraineeProfile_Then_ReturnsUnauthorizedResponseOnInvalidCredentials() {
        String username = "test";
        String password = "password";
        TraineeUpdateDTO updateDTO = new TraineeUpdateDTO();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<?> response = traineeController.updateTraineeProfile(username, password, updateDTO);

        verify(authenticateService).matchUserCredentials(username, password);
        assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void Given_TraineeServiceInitialized_When_UpdatingTraineeProfile_Then_ReturnsInternalServerErrorOnServerError() {
        String username = "test";
        String password = "password";
        TraineeUpdateDTO updateDTO = new TraineeUpdateDTO();
        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(traineeService.updateTraineeProfile(anyString(), anyString(), anyString(), anyString(), any(), anyString(), anyBoolean())).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = traineeController.updateTraineeProfile(username, password, updateDTO);

        verify(authenticateService).matchUserCredentials(username, password);
        assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void Given_TraineeServiceInitialized_When_UpdatingTraineeStatus_Then_ReturnsOkResponseOnSuccess() {
        String username = "test";
        boolean isActive = true;

        ResponseEntity<String> response = traineeController.updateTrainerStatus(username, isActive);

        verify(traineeService).updateTraineeStatus(username, isActive);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trainer status updated successfully!", response.getBody());
    }

    @Test
    public void Given_TraineeServiceInitialized_When_GettingTraineeTrainingsList_Then_ReturnsOkResponseOnSuccess() {
        String username = "test";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingTypeName = "training";
        String password = "password";
        List<TrainingDTO> trainings = Collections.singletonList(new TrainingDTO());

        when(traineeService.getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName)).thenReturn(trainings);

        ResponseEntity<List<TrainingDTO>> response = traineeController.getTraineeTrainingsList(username, fromDate, toDate, trainerName, trainingTypeName, password);

        verify(traineeService).getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainings, response.getBody());
    }


    @Test
    public void Given_TraineeServiceInitialized_When_GettingTraineeTrainingsList_Then_ReturnsInternalServerErrorOnServerError() {
        String username = "test";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingTypeName = "training";
        String password = "password";

        when(traineeService.getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName)).thenThrow(RuntimeException.class);

        ResponseEntity<List<TrainingDTO>> response = traineeController.getTraineeTrainingsList(username, fromDate, toDate, trainerName, trainingTypeName, password);

        verify(traineeService).getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


    @Test
    public void updateTraineeTrainerList_ValidRequest_Success() {
        String traineeUsername = "trainee";
        List<String> trainerUsernames = Collections.singletonList("trainer");
        List<TrainerResponse> updatedTrainers = Collections.singletonList(new TrainerResponse());

        when(traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames)).thenReturn(updatedTrainers);

        ResponseEntity<List<TrainerResponse>> response = traineeController.updateTraineeTrainerList(traineeUsername, trainerUsernames);

        verify(traineeService).updateTraineeTrainerList(traineeUsername, trainerUsernames);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTrainers, response.getBody());
    }


    @Test
    public void updateTraineeTrainerList_InternalServerError_ReturnsInternalServerError() {
        String traineeUsername = "trainee";
        List<String> trainerUsernames = Collections.singletonList("trainer");

        when(traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames)).thenThrow(new RuntimeException("Expected message for the RuntimeException"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            traineeController.updateTraineeTrainerList(traineeUsername, trainerUsernames);
        });

        verify(traineeService).updateTraineeTrainerList(traineeUsername, trainerUsernames);
        assertEquals("Expected message for the RuntimeException", exception.getMessage());
    }

    @Test
    public void updateTraineeTrainerList_Success_ReturnsOk() {
        String traineeUsername = "trainee";
        List<String> trainerUsernames = Collections.singletonList("trainer");
        List<TrainerResponse> updatedTrainers = Collections.singletonList(new TrainerResponse());

        when(traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames)).thenReturn(updatedTrainers);

        ResponseEntity<List<TrainerResponse>> response = traineeController.updateTraineeTrainerList(traineeUsername, trainerUsernames);

        verify(traineeService).updateTraineeTrainerList(traineeUsername, trainerUsernames);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTrainers, response.getBody());
    }

    @Test
    public void Given_TraineeServiceInitialized_When_UpdatingTraineeTrainersList_Then_ReturnsNotFoundResponseIfTraineeNotFound() {
        String traineeUsername = "trainee";
        List<String> trainerUsernames = Collections.singletonList("trainer");

        when(traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames)).thenReturn(null);

        ResponseEntity<List<TrainerResponse>> response = traineeController.updateTraineeTrainerList(traineeUsername, trainerUsernames);

        verify(traineeService).updateTraineeTrainerList(traineeUsername, trainerUsernames);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
