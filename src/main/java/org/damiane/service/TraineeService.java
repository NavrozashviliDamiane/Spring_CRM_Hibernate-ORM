package org.damiane.service;

import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainer.TrainerResponse;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.entity.Trainee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public interface TraineeService {

    List<Trainee> getAllTrainees(String username, String password);

    TraineeProfileDTO getTraineeProfile(String username);

    Trainee getTraineeById(Long id, String username, String password);

    Trainee getTraineeByUsername(String username, String password);


    @Transactional
    Trainee updateTraineeProfile(String username, String firstName, String password, String lastName,
                                 Date dateOfBirth, String address, boolean isActive);

    void changeTraineePassword(Long traineeId, String username, String password, String newPassword);

    Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address);


    void activateTrainee(String username, boolean isActive);

    @Transactional
    void updateTraineeStatus(String username, boolean isActive);

    void deactivateTrainee(String username, boolean isActive);


    // TraineeService.java
    @Transactional
    void deleteTraineeByUsername(String username);

    List<TrainingDTO> getTraineeTrainingsList(String username, String password, Date fromDate, Date toDate,
                                              String trainerName, String trainingTypeName);

    List<TrainerResponse> updateTraineeTrainerList(String traineeUsername, List<String> trainerUsernames);
}
