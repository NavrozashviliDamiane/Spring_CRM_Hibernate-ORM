package org.damiane.service;

import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TrainerService {

    Trainer getTrainerByUsername(String username);

    void changeTrainerPassword(Long trainerId, String newPassword);

    boolean matchTrainerCredentials(String username, String password);

    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);


    Trainer updateTrainerProfile(String username, String firstName, String lastName, TrainingTypeValue trainingTypeValue);

    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType);

    void deleteTrainer(Long id);

    // TrainerServiceImpl.java
    void activateTrainer(Long trainerId);

    void deactivateTrainer(Long trainerId);

    List<Trainer> findUnassignedTrainersByTraineeUsername(String traineeUsername, String password);
}
