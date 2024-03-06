package org.damiane.service;

import org.damiane.entity.Training;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TrainingService {

    List<Training> getAllTrainings(String username, String password);

    Training getTrainingById(Long id, String username, String password);

    Training createTraining(String traineeUsername, String trainerUsername, TrainingTypeValue trainingTypeValue,
                            String trainingName, Date trainingDate, Integer trainingDuration,
                            String password);

    void deleteTraining(Long id, String username, String password);

    void updateTrainingForTrainee(String username);

    List<Training> getTrainingsByTraineeUsernameAndCriteria(String username, String password, Date fromDate, Date toDate, String trainerName, TrainingTypeValue trainingTypeName);

    List<Training> getTrainingsByTrainerUsernameAndCriteria(String username, String password, Date fromDate, Date toDate, String traineeName);
}
