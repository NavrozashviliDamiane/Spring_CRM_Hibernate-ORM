package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.TrainerRegistrationRequest;
import org.damiane.entity.*;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TrainerService;
import org.damiane.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final AuthenticateService authenticateService;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService, TrainingTypeRepository trainingTypeRepository, TraineeRepository traineeRepository, TrainingRepository trainingRepository, AuthenticateService authenticateService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.authenticateService = authenticateService;
    }

    @Override
    public Trainer getTrainerByUsername(String username, String password ) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");
        return trainerRepository.findByUserUsername(username);
    }

    @Override
    @Transactional
    public void changeTrainerPassword(Long trainerId, String username, String password, String newPassword) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        trainerOptional.ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userService.saveUser(user);
                log.info("Trainer Password Changed Successfully");
            }
        });
    }

    @Override
    public List<Trainer> getAllTrainers(String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return trainerRepository.findAll();
    }

    @Override
    public Trainer getTrainerById(Long id, String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return trainerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Trainer updateTrainerProfile(String username, String password, String firstName,
                                        String lastName, TrainingTypeValue trainingTypeValue) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainer trainer = trainerRepository.findByUserUsername(username);

        TrainingType trainingType = trainingTypeRepository.findByTrainingType(trainingTypeValue);

        User user = trainer.getUser();

        user.setFirstName(Objects.requireNonNullElse(firstName, user.getFirstName()));
        user.setLastName(Objects.requireNonNullElse(lastName, user.getLastName()));
        trainer.setTrainingType(Objects.requireNonNullElse(trainingType, trainer.getTrainingType()));

        userService.saveUser(user);

        log.info("Trainer Updated Successfully");

        return trainerRepository.save(trainer);

    }

    @Override
    @Transactional
    public Trainer createTrainer(TrainerRegistrationRequest request) {
        User user = userService.createUser(request.getFirstName(), request.getLastName());

        TrainingTypeValue trainingTypeValue = TrainingTypeValue.valueOf(request.getSpecialization().toUpperCase());
        TrainingType trainingType = new TrainingType(trainingTypeValue);

        // Check if trainingType already exists in the database
        TrainingType existingTrainingType = trainingTypeRepository.findByTrainingType(trainingType.getTrainingType());
        if (existingTrainingType != null) {
            trainingType = existingTrainingType;
        } else {
            trainingType = trainingTypeRepository.save(trainingType);
        }

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        log.info("Trainer created Successfully");
        return trainerRepository.save(trainer);
    }


    @Override
    public void deleteTrainer(Long id, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        trainerRepository.deleteById(id);
        log.info("Trainer Deleted Successfully");
    }

    @Override
    public void activateTrainer(Long trainerId,  String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");


        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer != null) {
            User user = trainer.getUser();
            user.setActive(true);
            userService.saveUser(user);

            trainerRepository.save(trainer);
            log.info("Trainer Activated Successfully");
        }
    }

    @Override
    public void deactivateTrainer(Long trainerId, String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer != null) {
            User user = trainer.getUser();
            user.setActive(false);
            userService.saveUser(user);

            trainerRepository.save(trainer);
            log.info("Trainer Deactivated Successfully");
        }
    }

    public List<Trainer> findUnassignedTrainersByTraineeUsername(String traineeUsername, String password) {
        authenticateService.matchUserCredentials(traineeUsername, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername);
        List<Training> trainingsWithTrainee = trainingRepository.findByTraineeId(trainee.getId());

        return trainerRepository.findAll().stream()
                .filter(trainer -> trainingsWithTrainee.stream()
                        .noneMatch(training -> Objects.equals(training.getTrainer(), trainer)))
                .collect(Collectors.toList());
    }

}
