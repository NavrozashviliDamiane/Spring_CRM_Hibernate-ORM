package org.damiane.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.dto.trainer.TrainerProfileDTO;
import org.damiane.dto.trainer.TrainerRegistrationRequest;
import org.damiane.dto.trainer.TrainerRegistrationResponse;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.User;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.service.TrainerService;
import org.damiane.service.impl.AuthenticateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private AuthenticateServiceImpl authenticateService;

    @Mock
    private Trainer trainer;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainerController trainerController;

    private TrainingType trainingType;
    private User user;

    @Test
    void registerTrainer_ReturnsCreatedResponse_WhenTrainerCreatedSuccessfully() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setSpecialization("Fitness");

        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("John.Doe");
        user.setPassword("password");
        trainer.setUser(user);

        when(trainerService.createTrainer(request)).thenReturn(trainer);

        ResponseEntity<TrainerRegistrationResponse> responseEntity = trainerController.registerTrainer(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("John.Doe", responseEntity.getBody().getUsername());
        assertEquals("password", responseEntity.getBody().getPassword());
    }

    @Test
    void registerTrainer_ReturnsInternalServerError_WhenTrainerCreationFails() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setSpecialization("Fitness");

        when(trainerService.createTrainer(request)).thenThrow(new RuntimeException("Failed to create trainer"));

        ResponseEntity<TrainerRegistrationResponse> responseEntity = trainerController.registerTrainer(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }


    @Test
    public void testGetTrainerProfile_Success() {
        String username = "testuser";
        String password = "testpassword";
        TrainerProfileDTO profileDTO = new TrainerProfileDTO();
        profileDTO.setFirstName("John");
        profileDTO.setLastName("Doe");

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerService.getTrainerProfile(username, password)).thenReturn(profileDTO);

        ResponseEntity<?> response = trainerController.getTrainerProfile(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDTO, response.getBody());

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerService).getTrainerProfile(username, password);
    }

    @Test
    public void testGetTrainerProfile_Unauthorized() {
        String username = "testuser";
        String password = "testpassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<?> response = trainerController.getTrainerProfile(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerService, never()).getTrainerProfile(anyString(), anyString());
    }

    @Test
    public void testGetTrainerProfile_TrainerNotFound() {
        String username = "testuser";
        String password = "testpassword";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerService.getTrainerProfile(username, password)).thenReturn(null);

        ResponseEntity<?> response = trainerController.getTrainerProfile(username, password);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainer not found", response.getBody());

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerService).getTrainerProfile(username, password);
    }


    @Test
    void testGetUnassignedActiveTrainersByTraineeUsername_Success() {
        // Mocking authenticateService
        when(authenticateService.matchUserCredentials("traineeUsername", "password")).thenReturn(true);

        // Mocking trainerService
        List<TrainerDTO> trainers = new ArrayList<>();
        // Add some mock data if needed
        when(trainerService.findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password")).thenReturn(trainers);

        // Call the method
        ResponseEntity<List<TrainerDTO>> responseEntity = trainerController.getUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(trainers, responseEntity.getBody());

        // Verify interactions
        verify(authenticateService, times(1)).matchUserCredentials("traineeUsername", "password");
        verify(trainerService, times(1)).findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");
    }

    @Test
    void testGetUnassignedActiveTrainersByTraineeUsername_Unauthorized() {
        // Mocking authenticateService to return false, indicating invalid credentials
        when(authenticateService.matchUserCredentials("invalidUsername", "invalidPassword")).thenReturn(false);

        // Call the method with invalid credentials
        ResponseEntity<List<TrainerDTO>> responseEntity = trainerController.getUnassignedActiveTrainersByTraineeUsername("invalidUsername", "invalidPassword");

        // Verify
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());

        // Verify interactions
        verify(authenticateService, times(1)).matchUserCredentials("invalidUsername", "invalidPassword");
        // Ensure trainerService is not invoked
        verifyNoInteractions(trainerService);
    }



}
