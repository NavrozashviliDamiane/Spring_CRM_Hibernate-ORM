//package org.damiane.service.impl;
//
//import org.damiane.entity.*;
//import org.damiane.repository.TraineeRepository;
//import org.damiane.repository.TrainerRepository;
//import org.damiane.repository.TrainingRepository;
//import org.damiane.repository.TrainingTypeRepository;
//import org.damiane.service.AuthenticateService;
//import org.damiane.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.mockito.Mockito.*;
//
//class TrainerServiceImplTest {
//
//    @Mock
//    private TrainerRepository trainerRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private TrainingTypeRepository trainingTypeRepository;
//
//    @Mock
//    private TraineeRepository traineeRepository;
//
//    @Mock
//    private TrainingRepository trainingRepository;
//
//    @Mock
//    private AuthenticateService authenticateService;
//
//    @InjectMocks
//    private TrainerServiceImpl trainerService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getTrainerByUsername_Should_ReturnCorrectTrainer_When_ValidUsernameAndPassword() {
//        String username = "testUser";
//        String password = "testPassword";
//        Trainer expectedTrainer = new Trainer();
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(trainerRepository.findByUserUsername(username)).thenReturn(expectedTrainer);
//
//        Trainer actualTrainer = trainerService.getTrainerByUsername(username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(trainerRepository).findByUserUsername(username);
//        assertEquals(expectedTrainer, actualTrainer);
//    }
//
//    @Test
//    void changeTrainerPassword_Should_ChangePassword_When_ValidTrainerIdAndCredentials() {
//        Long trainerId = 1L;
//        String username = "testUser";
//        String password = "testPassword";
//        String newPassword = "newPassword";
//        Trainer trainer = new Trainer();
//        trainer.setUser(new User());
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
//
//        trainerService.changeTrainerPassword(trainerId, username, password, newPassword);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(trainerRepository).findById(trainerId);
//        assertEquals(newPassword, trainer.getUser().getPassword());
//    }
//
//    @Test
//    void getAllTrainers_Should_ReturnAllTrainers_When_ValidUsernameAndPassword() {
//        String username = "testUser";
//        String password = "testPassword";
//        List<Trainer> expectedTrainers = new ArrayList<>();
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(trainerRepository.findAll()).thenReturn(expectedTrainers);
//
//        List<Trainer> actualTrainers = trainerService.getAllTrainers(username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(trainerRepository).findAll();
//        assertEquals(expectedTrainers, actualTrainers);
//    }
//
//    @Test
//    void getTrainerById_Should_ReturnCorrectTrainer_When_ValidIdUsernameAndPassword() {
//        Long trainerId = 1L;
//        String username = "testUser";
//        String password = "testPassword";
//        Trainer expectedTrainer = new Trainer();
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(expectedTrainer));
//
//        Trainer actualTrainer = trainerService.getTrainerById(trainerId, username, password);
//
//        verify(authenticateService).matchUserCredentials(username, password);
//        verify(trainerRepository).findById(trainerId);
//        assertEquals(expectedTrainer, actualTrainer);
//    }
//
////    @Test
////    void updateTrainerProfile_Should_UpdateTrainerProfile_When_ValidUsernameAndPassword() {
////        String username = "testUser";
////        String password = "testPassword";
////        String firstName = "John";
////        String lastName = "Doe";
////        TrainingType trainingType = new TrainingType();
////        Trainer existingTrainer = new Trainer();
////        existingTrainer.setUser(new User());
////
////        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
////        when(trainerRepository.findByUserUsername(username)).thenReturn(existingTrainer);
////        when(trainingTypeRepository.findByTrainingType(any())).thenReturn(trainingType);
////        when(trainerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
////
////        Trainer updatedTrainer = trainerService.updateTrainerProfile(username, password, firstName, lastName, trainingType.getTrainingType());
////
////        assertEquals(firstName, updatedTrainer.getUser().getFirstName());
////        assertEquals(lastName, updatedTrainer.getUser().getLastName());
////        assertEquals(trainingType, updatedTrainer.getTrainingType());
////    }
//
////    @Test
////    void createTrainer_Should_CreateTrainer_When_ValidInput() {
////        String firstName = "John";
////        String lastName = "Doe";
////        TrainingType trainingType = new TrainingType(TrainingTypeValue.CARDIO);
////        Trainer createdTrainer = new Trainer();
////        createdTrainer.setUser(new User());
////        when(userService.createUser(firstName, lastName)).thenReturn(new User());
////        when(trainingTypeRepository.findByTrainingType(trainingType.getTrainingType())).thenReturn(trainingType);
////        when(trainerRepository.save(any())).thenReturn(createdTrainer);
////
////        Trainer actualTrainer = trainerService.createTrainer();
////
////        verify(userService).createUser(firstName, lastName);
////        verify(trainingTypeRepository).findByTrainingType(trainingType.getTrainingType());
////        verify(trainerRepository).save(any());
////        assertEquals(createdTrainer, actualTrainer);
////    }
//
//
//    @Test
//    void deactivateTrainer_Should_DeactivateTrainer_When_ValidUsernameAndPassword() {
//        Long trainerId = 1L;
//        String username = "testUser";
//        String password = "testPassword";
//        Trainer trainer = new Trainer();
//        trainer.setUser(new User());
//
//        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
//        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
//
//        trainerService.deactivateTrainer(trainerId, username, password);
//
//        assertFalse(trainer.getUser().isActive());
//        verify(trainerRepository, times(1)).save(any());
//    }
//
//    @Test
//    void findUnassignedTrainersByTraineeUsername_Should_ReturnUnassignedTrainers_When_ValidUsernameAndPassword() {
//        String traineeUsername = "testUser";
//        String password = "testPassword";
//        Trainee trainee = new Trainee();
//        trainee.setUser(new User());
//        List<Trainer> allTrainers = new ArrayList<>();
//        List<Training> trainingsWithTrainee = new ArrayList<>();
//
//        when(authenticateService.matchUserCredentials(traineeUsername, password)).thenReturn(true);
//        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);
//        when(trainerRepository.findAll()).thenReturn(allTrainers);
//        when(trainingRepository.findByTraineeId(any())).thenReturn(trainingsWithTrainee);
//
//        List<Trainer> unassignedTrainers = trainerService.findUnassignedTrainersByTraineeUsername(traineeUsername, password);
//
//    }
//
//}
