package org.damiane.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.damiane.dto.trainee.TraineeDTO;
import org.damiane.dto.trainer.*;
import org.damiane.entity.Trainer;
import org.damiane.entity.User;
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
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private AuthenticateServiceImpl authenticateService;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void GivenValidTrainerRegistrationRequest_WhenTrainerCreatedSuccessfully_ThenReturnCreatedResponse() {
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
    void GivenValidTrainerRegistrationRequest_WhenTrainerCreationFails_ThenReturnInternalServerError() {
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
    public void GivenValidCredentials_WhenGettingTrainerProfile_ThenReturnSuccessWithProfileDTO() {
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
    public void GivenInvalidCredentials_WhenGettingTrainerProfile_ThenReturnUnauthorized() {
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
    public void GivenValidCredentials_WhenGettingTrainerProfile_ThenReturnTrainerNotFound() {
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
    void GivenValidTraineeUsernameAndPassword_WhenGettingUnassignedActiveTrainers_ThenReturnSuccessWithTrainersList() {
        when(authenticateService.matchUserCredentials("traineeUsername", "password")).thenReturn(true);

        List<TrainerDTO> trainers = new ArrayList<>();
        when(trainerService.findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password")).thenReturn(trainers);

        ResponseEntity<List<TrainerDTO>> responseEntity = trainerController.getUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(trainers, responseEntity.getBody());

        verify(authenticateService, times(1)).matchUserCredentials("traineeUsername", "password");
        verify(trainerService, times(1)).findUnassignedActiveTrainersByTraineeUsername("traineeUsername", "password");
    }

    @Test
    void GivenInvalidUsernameAndPassword_WhenGettingUnassignedActiveTrainers_ThenReturnUnauthorized() {
        when(authenticateService.matchUserCredentials("invalidUsername", "invalidPassword")).thenReturn(false);

        ResponseEntity<List<TrainerDTO>> responseEntity = trainerController.getUnassignedActiveTrainersByTraineeUsername("invalidUsername", "invalidPassword");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());

        verify(authenticateService, times(1)).matchUserCredentials("invalidUsername", "invalidPassword");
        verifyNoInteractions(trainerService);
    }

    @Test
    public void GivenValidUsernameAndPassword_WhenUpdatingTrainerStatus_ThenReturnSuccessWithOkStatus() {
        String username = "test";
        boolean isActive = true;

        ResponseEntity<String> response = trainerController.updateTrainerStatus(username, isActive);

        verify(trainerService).updateTrainerStatus(username, isActive);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trainer status updated successfully!", response.getBody());
    }


    @Test
    public void GivenValidTrainerUpdateDTO_WhenUpdatingTrainerProfile_ThenReturnSuccessWithUpdatedProfile() {
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO(
                "trainer_username",
                "trainer_password",
                "John",
                "Doe",
                true,
                "Java Programming"
        );

        TrainerProfileDTO updatedProfile = new TrainerProfileDTO(
                "John",
                "Doe",
                "Java Programming",
                true,
                List.of(
                        new TraineeDTO("trainee1", "Alice", "password"),
                        new TraineeDTO("trainee2", "Bob", "password")
                )
        );

        when(trainerService.updateTrainerProfile(trainerUpdateDTO)).thenReturn(updatedProfile);

        ResponseEntity<?> response = trainerController.updateTrainerProfile(trainerUpdateDTO);

        verify(trainerService).updateTrainerProfile(trainerUpdateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    public void GivenValidTrainerUpdateDTO_WhenUpdatingTrainerProfile_ThenReturnTrainerNotFound() {
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO(
                "trainer_username",
                "trainer_password",
                "John",
                "Doe",
                true,
                "Java Programming"
        );

        when(trainerService.updateTrainerProfile(trainerUpdateDTO)).thenReturn(null);

        ResponseEntity<?> response = trainerController.updateTrainerProfile(trainerUpdateDTO);

        verify(trainerService).updateTrainerProfile(trainerUpdateDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainer not found", response.getBody());
    }


    @Test
    void GivenValidCredentialsAndDateRange_WhenGettingTrainerTrainings_ThenReturnSuccessWithTrainingsList() {
        String username = "test";
        String password = "password";
        Date periodFrom = new Date();
        Date periodTo = new Date();
        String traineeName = "trainee";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);

        List<TrainerTrainingResponseDTO> mockTrainingResponses = new ArrayList<>();

        when(trainerService.getTrainerTrainings(any())).thenReturn(mockTrainingResponses);

        ResponseEntity<List<TrainerTrainingResponseDTO>> response = trainerController.getTrainerTrainings(username, password, periodFrom, periodTo, traineeName);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerService).getTrainerTrainings(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainingResponses, response.getBody());
    }

    @Test
    void GivenInvalidCredentials_WhenGettingTrainerTrainings_ThenReturnUnauthorized() {
        String username = "test";
        String password = "password";
        Date periodFrom = new Date();
        Date periodTo = new Date();
        String traineeName = "trainee";

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(false);

        ResponseEntity<?> response = trainerController.getTrainerTrainings(username, password, periodFrom, periodTo, traineeName);

        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerService, never()).getTrainerTrainings(any());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody()); // Ensure the body is not null
        assertEquals("Invalid username or password", response.getBody());
    }
}
