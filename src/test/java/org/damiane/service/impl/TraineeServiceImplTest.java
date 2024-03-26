package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.entity.*;
import org.damiane.mapper.TrainingToTrainerMapper;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.service.UserService;
import org.damiane.util.trainee.GetTraineeTrainingsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private Trainee trainee;

    @Mock
    private GetTraineeTrainingsHelper getTraineeTrainingsHelper;

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
    void When_CreateTraineeWithValidData_Then_TraineeIsSuccessfullyCreated() {

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
    void When_GetTraineeProfileWithExistingUser_Then_ReturnTraineeProfileDTO() {
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
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);
        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username);

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
    void When_GetTraineeProfileWithNonExistingUser_Then_ReturnNull() {
        String username = "nonExistentUser";
        when(traineeRepository.findByUserUsername(username)).thenReturn(null);

        TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username);

        assertNull(profileDTO);
        verify(traineeRepository).findByUserUsername(username);
        verifyNoInteractions(trainingRepository, trainingToTrainerMapper);
    }




    @Test
    void When_UpdateTraineeProfileWithNonExistingUser_Then_ReturnNull() {
        String username = "nonExistentUser";
        when(traineeRepository.findByUserUsername(username)).thenReturn(null);

        Trainee updatedTrainee = traineeService.updateTraineeProfile(username, "John", "password", "Doe",
                new Date(), "123 Main St", true);

        assertNull(updatedTrainee);
        verify(authenticateService).matchUserCredentials(username, "password");
        verify(traineeRepository).findByUserUsername(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void When_UpdateTraineeProfileThrowsException_Then_ThrowRuntimeException() {
        String username = "testUser";
        when(traineeRepository.findByUserUsername(username)).thenThrow(new RuntimeException("Mocked exception"));

        assertThrows(RuntimeException.class, () -> traineeService.updateTraineeProfile(username, "John",
                "password", "Doe", new Date(), "123 Main St", true));

        verify(authenticateService).matchUserCredentials(username, "password");
        verify(traineeRepository).findByUserUsername(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void When_UpdateTraineeStatusWithExistingTrainee_Active_Then_UserIsActive() {
        String username = "trainee";
        boolean isActive = true;
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        traineeService.updateTraineeStatus(username, isActive);

        assertTrue(user.isActive());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void When_UpdateTraineeStatusWithNonExistingTrainee_Then_NoActionTaken() {
        String username = "nonexistent_trainee";
        boolean isActive = true;
        when(traineeRepository.findByUserUsername(username)).thenReturn(null);

        traineeService.updateTraineeStatus(username, isActive);

        verifyNoInteractions(userService);
    }

    @Test
    void When_DeleteExistingTraineeWithTrainings_Then_SuccessfullyDeleteTraineeAndTrainings() {
        String username = "existing_trainee";
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setId(1L);
        trainee.setUser(user);
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        traineeService.deleteTraineeByUsername(username);

        verify(trainingRepository, times(1)).findByTraineeId(trainee.getId());
        verify(trainingRepository, times(1)).deleteAll(trainings);
        verify(userService, times(1)).deleteUserById(user.getId());
        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    void When_DeleteExistingTraineeWithNoTrainings_Then_SuccessfullyDeleteTrainee() {
        String username = "existing_trainee";
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setId(1L);
        trainee.setUser(user);
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);
        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        traineeService.deleteTraineeByUsername(username);

        verify(trainingRepository, times(1)).findByTraineeId(trainee.getId());
        verify(trainingRepository, times(1)).deleteAll(trainings);
        verify(userService, times(1)).deleteUserById(user.getId());
        verify(traineeRepository, times(1)).delete(trainee);
    }


    @Test
    void When_GetTraineeTrainingsList_Then_SuccessfullyRetrieveTrainingsList() {
        String username = "trainee_username";
        String password = "trainee_password";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer_name";
        String trainingTypeName = "training_type_name";

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        Long trainerId = 0L;
        Long trainingTypeId = 0L;
        List<TrainingDTO> trainingDTOList = Collections.emptyList();
        when(getTraineeTrainingsHelper.getTrainerId(trainerName)).thenReturn(trainerId);
        when(getTraineeTrainingsHelper.getTrainingTypeId(trainingTypeName)).thenReturn(trainingTypeId);
        when(getTraineeTrainingsHelper.constructQuery(trainee.getId(), fromDate, toDate, trainerId, trainingTypeId))
                .thenReturn(Collections.emptyList());
        when(getTraineeTrainingsHelper.mapToTrainingDTO(Collections.emptyList()))
                .thenReturn(trainingDTOList);

        traineeService.getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName);

        verify(getTraineeTrainingsHelper, times(1)).constructQuery(trainee.getId(), fromDate, toDate, trainerId, trainingTypeId);
    }


    @Test
    void When_UpdateTraineeTrainerList_WithExistingTraineeAndTrainers_Then_SuccessfullyUpdateTraining() {
        String traineeUsername = "trainee_username";
        List<String> trainerUsernames = List.of("trainer1", "trainer2");

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);

        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setTrainingType(new TrainingType());
        when(trainerRepository.findByUserUsername("trainer1")).thenReturn(trainer1);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setTrainingType(new TrainingType());
        when(trainerRepository.findByUserUsername("trainer2")).thenReturn(trainer2);

        List<Training> trainings = new ArrayList<>();
        when(trainingRepository.findByTraineeId(trainee.getId())).thenReturn(trainings);

        traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames);


    }


}
