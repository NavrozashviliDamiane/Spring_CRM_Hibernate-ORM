package org.damiane.service;

import org.damiane.entity.Trainee;
import org.damiane.entity.Training;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingRepository.findById(id).orElse(null);
    }

    @Override
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    @Override
    public void updateTrainingForTrainee(String username) {
        // Find the trainee by username
        Trainee trainee = traineeRepository.findByUserUsername(username);

        if (trainee != null) {
            // Update the training records associated with the trainee to set trainee_id to NULL
            List<Training> trainings = trainingRepository.findByTraineeId(trainee.getId());
            for (Training training : trainings) {
                training.setTrainee(null);
                trainingRepository.save(training);
            }
        }
    }

    @Override
    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        // Implement the query based on the provided criteria
        return trainingRepository.findTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType);
    }
}
