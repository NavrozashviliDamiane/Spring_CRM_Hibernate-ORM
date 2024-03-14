package org.damiane.service;

import org.damiane.dto.TrainerRegistrationRequest;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface TrainerService {

    Trainer getTrainerByUsername(String username, String password);

    void changeTrainerPassword(Long trainerId, String username, String password, String newPassword);

    List<Trainer> getAllTrainers(String username, String password);

    Trainer getTrainerById(Long id, String username, String password);

    Trainer updateTrainerProfile(String username, String password, String firstName,
                                 String lastName, TrainingTypeValue trainingTypeValue);



    @Transactional
    Trainer createTrainer(TrainerRegistrationRequest request);

    void deleteTrainer(Long id, String username, String password);

    void activateTrainer(Long trainerId, String username, String password);

    void deactivateTrainer(Long trainerId, String username, String password);

    List<Trainer> findUnassignedTrainersByTraineeUsername(String traineeUsername, String password);
}
