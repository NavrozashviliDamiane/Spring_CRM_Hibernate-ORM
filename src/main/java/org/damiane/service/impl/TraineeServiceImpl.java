package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.entity.*;
import org.damiane.repository.TraineeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.damiane.service.TrainingService;
import org.damiane.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private TrainingService trainingService;

    @Override
    public List<Trainee> getAllTrainees(String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return traineeRepository.findAll();
    }

    @Override
    public Trainee getTraineeById(Long id, String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return traineeRepository.findById(id).orElse(null);
    }


    @Override
    public Trainee getTraineeByUsername(String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return traineeRepository.findByUserUsername(username);
    }

    @Override
    @Transactional
    public Trainee updateTraineeProfile(String username, String firstName, String password, String lastName,
                                        Date dateOfBirth, String address) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findByUserUsername(username);


        User user = trainee.getUser();

        user.setFirstName(Objects.requireNonNullElse(firstName, user.getFirstName()));
        user.setLastName(Objects.requireNonNullElse(lastName, user.getLastName()));
        trainee.setDateOfBirth(Objects.requireNonNullElse(dateOfBirth, trainee.getDateOfBirth()));
        trainee.setAddress(Objects.requireNonNullElse(address, trainee.getAddress()));

        userService.saveUser(user);

        log.info("Trainee Profile Updated Successfully");
        return traineeRepository.save(trainee);


    }

    @Override
    @Transactional
    public void changeTraineePassword(Long traineeId, String username, String password, String newPassword) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        traineeOptional.ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userService.saveUser(user);
                log.info("Password Changed Successfully");
            }
        });
    }

    @Override
    @Transactional
    public Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address) {

        User user = userService.createUser(firstName, lastName);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);

        log.info("Trainee Created Successfully");
        return traineeRepository.save(trainee);
    }


    @Override
    @Transactional
    public void activateTrainee(Long traineeId, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findById(traineeId).orElse(null);
        if (trainee != null) {
            User user = trainee.getUser();
            user.setActive(true);
            userService.saveUser(user);

            traineeRepository.save(trainee);
            log.info("Trainee activated Successfully!");
        }
    }

    @Override
    @Transactional
    public void deactivateTrainee(Long traineeId, String username, String password) {

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findById(traineeId).orElse(null);
        if (trainee != null) {
            User user = trainee.getUser();
            user.setActive(false);
            userService.saveUser(user);

            traineeRepository.save(trainee);
            log.info("Trainee deactivated Successfully!");
        }
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        trainingService.updateTrainingForTrainee(username);

        Trainee trainee = traineeRepository.findByUserUsername(username);

        if (trainee != null) {
            traineeRepository.delete(trainee);
            log.info("Trainee deleted Successfully");
        }
    }
}
