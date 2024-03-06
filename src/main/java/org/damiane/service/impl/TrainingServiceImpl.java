package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.entity.*;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
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

    @Autowired
    private AuthenticateService authenticateService;

    @Override
    public List<Training> getAllTrainings(String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return trainingRepository.findAll();
    }

    @Override
    public Training getTrainingById(Long id, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");


        return trainingRepository.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public Training createTraining(String traineeUsername, String trainerUsername, TrainingTypeValue trainingTypeValue,
                                   String trainingName, Date trainingDate, Integer trainingDuration,
                                   String password){

        authenticateService.matchUserCredentials(trainerUsername, password);
        log.info("User Authenticated Successfully");


        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername);

        Trainer trainer = trainerRepository.findByUserUsername(trainerUsername);

        TrainingType trainingTypeIs = trainingTypeRepository.findByTrainingType(trainingTypeValue);

        Training training = new Training();

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingTypeIs);
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        log.info("Training Created Successfully");
        return trainingRepository.save(training);


    }

    @Override
    @Transactional
    public void deleteTraining(Long id, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        trainingRepository.deleteById(id);
        log.info("Training deleted Successfully");
    }

    @Override
    @Transactional
    public void updateTrainingForTrainee(String username) {
        Trainee trainee = traineeRepository.findByUserUsername(username);

        if (trainee != null) {
            List<Training> trainings = trainingRepository.findByTraineeId(trainee.getId());
            for (Training training : trainings) {
                training.setTrainee(null);
                trainingRepository.save(training);
                log.info("Updated Training Successfully");
            }
        }
    }

    @Override
    public List<Training> getTrainingsByTraineeUsernameAndCriteria(String username, String password, Date fromDate, Date toDate, String trainerName, TrainingTypeValue trainingTypeName) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findByUserUsername(username);

        Trainer trainer = trainerRepository.findByUserUsername(trainerName);

        TrainingType trainingTypeIs = trainingTypeRepository.findByTrainingType(trainingTypeName);

        if (trainee == null) {
            return List.of();
        }

        Long traineeId = trainee.getId();

        Long trainerId = trainer.getId();

        Long trainingTypeId = trainingTypeIs.getId();

        return trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                traineeId, fromDate, toDate, trainerId, trainingTypeId);
    }


    @Override
    public List<Training> getTrainingsByTrainerUsernameAndCriteria(String username, String password, Date fromDate, Date toDate, String traineeName) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

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
