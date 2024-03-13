package org.damiane.service;

import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TrainerService {

    Trainer getTrainerByUsername(String username, String password);

    void changeTrainerPassword(Long trainerId, String username, String password, String newPassword);

    List<Trainer> getAllTrainers(String username, String password);

    Trainer getTrainerById(Long id, String username, String password);

    Trainer updateTrainerProfile(String username, String password, String firstName,
                                 String lastName, TrainingTypeValue trainingTypeValue);


    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType);

    void deleteTrainer(Long id, String username, String password);

    void activateTrainer(Long trainerId, String username, String password);

    void deactivateTrainer(Long trainerId, String username, String password);

    List<Trainer> findUnassignedTrainersByTraineeUsername(String traineeUsername, String password);
}
