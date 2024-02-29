package org.damiane.service;

import org.damiane.entity.Training;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TrainingService {
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    Training saveTraining(Training training);

    Training createTraining(String traineeUsername, String trainerUsername, TrainingTypeValue trainingTypeValue, String trainingName, Date trainingDate, Integer trainingDuration);

    void deleteTraining(Long id);


    void updateTrainingForTrainee(String username);

    List<Training> getTrainingsByTraineeUsernameAndCriteria(String username, Date fromDate, Date toDate, String trainerName, TrainingTypeValue trainingTypeName);

    List<Training> getTrainingsByTrainerUsernameAndCriteria(String username, Date fromDate, Date toDate, String traineeName);
}
