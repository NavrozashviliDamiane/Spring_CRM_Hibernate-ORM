package org.damiane.service;

import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainer.TrainerResponse;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.entity.Trainee;
import org.damiane.exception.TraineeNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public interface TraineeService {

    TraineeProfileDTO getTraineeProfile(String username);


    @Transactional
    Trainee updateTraineeProfile(String username, String firstName, String password, String lastName,
                                 Date dateOfBirth, String address, boolean isActive);


    Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address);



    @Transactional
    void updateTraineeStatus(String username, boolean isActive);



    @Transactional
    void deleteTraineeByUsername(String username);

    List<TrainingDTO> getTraineeTrainingsList(String username, String password, Date fromDate, Date toDate,
                                              String trainerName, String trainingTypeName);

    List<TrainerResponse> updateTraineeTrainerList(String traineeUsername, List<String> trainerUsernames) throws TraineeNotFoundException;
}
