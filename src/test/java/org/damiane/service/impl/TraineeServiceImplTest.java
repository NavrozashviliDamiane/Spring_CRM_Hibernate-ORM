package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.TrainingDTO;
import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.entity.*;
import org.damiane.mapper.TrainingToTrainerMapper;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.service.UserService;
import org.damiane.util.trainee.GetTraineeTrainingsHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private Trainee trainee;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private AuthenticateServiceImpl authenticateService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingToTrainerMapper trainingToTrainerMapper;

    @InjectMocks
    private GetTraineeTrainingsHelper trainingsHelper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void createTrainee_CreatesTraineeSuccessfully_WhenValidDataProvided() {

        String firstName = "Damiane";
        String lastName = "Navro";
        Date dateOfBirth = new Date();
        String address = "123 Main St";

        User user = new User();
        user.setUsername("Damiane.Navro");
        user.setPassword("password");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(userService.createUser(firstName, lastName)).thenReturn(user);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        verify(userService).createUser(firstName, lastName);
        verify(traineeRepository).save(any(Trainee.class));

        assertNotNull(createdTrainee);
        assertEquals(user, createdTrainee.getUser());
    }


    @Test
    void getTraineeProfile_CreatesTraineeSuccessfully_WhenUserExists() {
        // Mocking data
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Main St");
        List<Training> trainings = new ArrayList<>();
        // Add some mock training objects if needed
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);
        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        // Invoke the method
        TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username);

        // Verify the interactions and assertions
        assertEquals(user.getFirstName(), profileDTO.getFirstName());
        assertEquals(user.getLastName(), profileDTO.getLastName());
        assertEquals(trainee.getDateOfBirth(), profileDTO.getDateOfBirth());
        assertEquals(trainee.getAddress(), profileDTO.getAddress());
        assertEquals(user.isActive(), profileDTO.isActive());
        verify(traineeRepository).findByUserUsername(username);
        verify(trainingRepository).findByTraineeId(trainee.getId());
        verifyNoInteractions(trainingToTrainerMapper);
    }

    @Test
    void getTraineeProfile_TraineeNotFound_WhenUserDoesNotExist() {
        // Mocking data
        String username = "nonExistentUser";
        when(traineeRepository.findByUserUsername(username)).thenReturn(null);

        // Invoke the method
        TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username);

        assertNull(profileDTO);
        verify(traineeRepository).findByUserUsername(username);
        verifyNoInteractions(trainingRepository, trainingToTrainerMapper); // No interactions with these mocks
    }




    @Test
    void updateTraineeProfile_TraineeNotFound() {
        // Mocking data
        String username = "nonExistentUser";
        when(traineeRepository.findByUserUsername(username)).thenReturn(null);

        // Invoke the method
        Trainee updatedTrainee = traineeService.updateTraineeProfile(username, "John", "password", "Doe",
                new Date(), "123 Main St", true);

        // Verify interactions and assertions
        assertNull(updatedTrainee);
        verify(authenticateService).matchUserCredentials(username, "password");
        verify(traineeRepository).findByUserUsername(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void updateTraineeProfile_ThrowsException_WhenErrorOccurs() {
        // Mocking data
        String username = "testUser";
        when(traineeRepository.findByUserUsername(username)).thenThrow(new RuntimeException("Mocked exception"));

        // Invoke the method
        assertThrows(RuntimeException.class, () -> traineeService.updateTraineeProfile(username, "John",
                "password", "Doe", new Date(), "123 Main St", true));

        // Verify interactions
        verify(authenticateService).matchUserCredentials(username, "password");
        verify(traineeRepository).findByUserUsername(username);
        verifyNoMoreInteractions(userService);
    }



}
