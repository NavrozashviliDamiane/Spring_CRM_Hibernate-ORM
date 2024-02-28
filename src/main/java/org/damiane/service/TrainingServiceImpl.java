package org.damiane.service;

import org.damiane.entity.*;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
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

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

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
    public List<Training> getTrainingsByTraineeUsernameAndCriteria(String username, Date fromDate, Date toDate, String trainerName, TrainingTypeValue trainingTypeName) {
        // Find the trainee by username
        Trainee trainee = traineeRepository.findByUserUsername(username);

        Trainer trainer = trainerRepository.findByUserUsername(trainerName);

        TrainingType trainingTypeIs = trainingTypeRepository.findByTrainingType(trainingTypeName);


        if (trainee == null) {
            // Trainee not found, return empty list or handle appropriately
            return List.of();
        }

        // Get trainee ID
        Long traineeId = trainee.getId();

        Long trainerId = trainer.getId();

        Long trainingTypeId = trainingTypeIs.getId();

        // Use trainee ID to query trainings
        return trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                traineeId, fromDate, toDate, trainerId, trainingTypeId);
    }


    @Override
    public List<Training> getTrainingsByTrainerUsernameAndCriteria(String username, Date fromDate, Date toDate, String traineeName) {

        Trainer trainer = trainerRepository.findByUserUsername(username);
        Trainee trainee = traineeRepository.findByUserUsername(traineeName);


        if (trainer == null) {
            return List.of();
        }

        Long trainerId = trainer.getId();

        Long traineeId = trainee.getId();


        return trainingRepository.findByTrainerIdAndTrainingDateBetweenAndTraineeId(
                trainerId, fromDate, toDate, traineeId);
    }


}
