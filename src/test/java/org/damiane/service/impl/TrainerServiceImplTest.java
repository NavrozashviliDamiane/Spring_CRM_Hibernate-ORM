package org.damiane.service.impl;

import org.damiane.entity.Trainer;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        user.setPassword(password);
        trainer.setUser(user);

        when(authenticateService.matchUserCredentials(username, password)).thenReturn(true);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        trainerService.changeTrainerPassword(trainerId, username, password, newPassword);

        assertEquals(newPassword, trainer.getUser().getPassword());
        verify(authenticateService).matchUserCredentials(username, password);
        verify(trainerRepository).findById(trainerId);
        verify(userService).saveUser(any());
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
}
