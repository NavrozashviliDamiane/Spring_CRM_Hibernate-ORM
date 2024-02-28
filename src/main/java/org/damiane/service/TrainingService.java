package org.damiane.service;

import org.damiane.entity.Training;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrainingService {
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    Training saveTraining(Training training);
    void deleteTraining(Long id);
}
