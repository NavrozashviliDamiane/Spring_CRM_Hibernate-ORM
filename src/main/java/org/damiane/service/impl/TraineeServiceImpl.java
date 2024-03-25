package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.dto.trainer.TrainerResponse;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.entity.*;
import org.damiane.mapper.TrainingToTrainerMapper;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.damiane.service.TrainingService;
import org.damiane.service.UserService;
import org.damiane.util.trainee.GetTraineeTrainingsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final AuthenticateService authenticateService;
    private final TrainingService trainingService;

    private final TrainingToTrainerMapper trainingToTrainerMapper;

    private final GetTraineeTrainingsHelper getTraineeTrainingsHelper;


    private final TrainingTypeRepository trainingTypeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, UserService userService,
                              AuthenticateService authenticateService,
                              TrainingService trainingService, TrainingRepository trainingRepository,
                              TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository,
                              TrainingToTrainerMapper trainingToTrainerMapper,  GetTraineeTrainingsHelper getTraineeTrainingsHelper
    ) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.authenticateService = authenticateService;
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingToTrainerMapper = trainingToTrainerMapper;
        this.getTraineeTrainingsHelper = getTraineeTrainingsHelper;
    }




    @Override
    public TraineeProfileDTO getTraineeProfile(String username) {
        log.info("Fetching trainee profile for username: {}", username);
        try {
            Trainee trainee = traineeRepository.findByUserUsername(username);
            if (trainee == null) {
                log.info("Trainee not found for username: {}", username);
                return null;
            }

            User user = trainee.getUser();

            TraineeProfileDTO profileDTO = new TraineeProfileDTO();
            profileDTO.setFirstName(user.getFirstName());
            profileDTO.setLastName(user.getLastName());
            profileDTO.setDateOfBirth(trainee.getDateOfBirth());
            profileDTO.setAddress(trainee.getAddress());
            profileDTO.setActive(user.isActive());

            List<Training> trainings = trainingRepository.findByTraineeId(trainee.getId());
            List<TrainerDTO> trainers = trainings.stream()
                    .map(trainingToTrainerMapper::mapTrainingToTrainerDTO)
                    .toList();

            profileDTO.setTrainers(trainers);

            log.info("Trainee profile fetched successfully for username: {}", username);
            return profileDTO;
        } catch (Exception ex) {
            log.error("Error occurred while fetching trainee profile for username: {}", username, ex);
            throw ex;
        }
    }



    @Override
    @Transactional
    public Trainee updateTraineeProfile(String username, String firstName, String password, String lastName,
                                        Date dateOfBirth, String address, boolean isActive) {

        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for trainee update. Transaction ID: {}", transactionId);

        try {

            authenticateService.matchUserCredentials(username, password);
            Trainee trainee = traineeRepository.findByUserUsername(username);

            if (trainee != null) {
                User user = trainee.getUser();
                user.setFirstName(firstName != null ? firstName : user.getFirstName());
                user.setLastName(lastName != null ? lastName : user.getLastName());
                trainee.setDateOfBirth(dateOfBirth != null ? dateOfBirth : trainee.getDateOfBirth());
                trainee.setAddress(address != null ? address : trainee.getAddress());
                user.setActive(isActive);
                userService.saveUser(user);
                log.info("Transaction successful for trainee update. Transaction ID: {}", transactionId);

                return traineeRepository.save(trainee);


            } else {
                return null;
            }

        } catch (Exception e) {
            log.info("Error occurred while updating trainee. Transaction ID: {}", transactionId, e);
            throw e;
        }
    }




    @Override
    @Transactional
    public Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address) {

        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for trainee creation. Transaction ID: {}", transactionId);

        try {
            User user = userService.createUser(firstName, lastName);

            Trainee trainee = new Trainee();
            trainee.setUser(user);
            trainee.setDateOfBirth(dateOfBirth);
            trainee.setAddress(address);

            log.info("Trainee Created Successfully");
            Trainee createdTrainee = traineeRepository.save(trainee);

            log.info("Trainee created successfully. Transaction ID: {}", transactionId);
            return createdTrainee;
        } catch (Exception e) {
            log.info("Error occurred while creating trainee. Transaction ID: {}", transactionId, e);
            throw e;
        }
    }



    @Override
    @Transactional
    public void updateTraineeStatus(String username, boolean isActive) {
        Trainee trainee = traineeRepository.findByUserUsername(username);
        if (trainee != null) {
            User user = trainee.getUser();
            user.setActive(isActive);
            userService.saveUser(user);
            log.info("Trainee status updated Successfully!");
        } else {
            log.error("Trainee not found with username: " + username);
        }
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {

        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for trainee delete. Transaction ID: {}", transactionId);

        Trainee trainee = traineeRepository.findByUserUsername(username);

        List<Training> trainings = trainingRepository.findByTraineeId(trainee.getId());
        trainingRepository.deleteAll(trainings);
        log.info("Transaction successful of deleting trainee's associate training. Transaction ID: {}", transactionId);

        userService.deleteUserById(trainee.getUser().getId());
        log.info("Transaction successful of deleting trainee's associate user. Transaction ID: {}", transactionId);
        traineeRepository.delete(trainee);
        log.info("Transaction successful of deleting trainee. Transaction ID: {}", transactionId);

        log.info("Trainee, associated trainings, and user deleted successfully");
    }

    @Override
    public List<TrainingDTO> getTraineeTrainingsList(String username, String password, Date fromDate, Date toDate,
                                                     String trainerName, String trainingTypeName) {

        authenticateService.matchUserCredentials(username, password);

        Trainee trainee = traineeRepository.findByUserUsername(username);
        if (trainee == null) {
            log.error("Trainee not found!");
            return Collections.emptyList();
        }

        Long traineeId = trainee.getId();
        Long trainerId = getTraineeTrainingsHelper.getTrainerId(trainerName);
        Long trainingTypeId = getTraineeTrainingsHelper.getTrainingTypeId(trainingTypeName);

        List<Training> trainings = getTraineeTrainingsHelper.constructQuery(traineeId, fromDate, toDate, trainerId, trainingTypeId);

        return getTraineeTrainingsHelper.mapToTrainingDTO(trainings);
    }



    @Override
    public List<TrainerResponse> updateTraineeTrainerList(String traineeUsername, List<String> trainerUsernames) {

        log.info("Updating trainee's trainer list for trainee: {}", traineeUsername);

        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername);

        if (trainee == null) {
            log.info("Trainee not found with username: {}", traineeUsername);

            return null;
        }

        Long traineeId = trainee.getId();
        log.info("Trainee found with ID: {}", traineeId);

        List<Training> trainings = trainingRepository.findByTraineeId(traineeId);
        log.info("Found {} trainings associated with trainee", trainings.size());


        for (String trainerUsername : trainerUsernames) {
            Trainer trainer = trainerRepository.findByUserUsername(trainerUsername);
            if (trainer != null) {
                log.info("Trainer found with username: {}", trainerUsername);
                Long trainerTrainingTypeId = trainer.getTrainingType().getId();

                for (Training training : trainings) {
                    Long trainingTrainingTypeId = training.getTrainingType().getId();
                    if (trainingTrainingTypeId.equals(trainerTrainingTypeId)) {
                        training.setTrainer(trainer);
                        trainingRepository.save(training);
                        log.info("Training updated with new trainer: {}", trainer.getUser());
                    }
                }
            }
        }


        List<TrainerResponse> updatedTrainers = new ArrayList<>();
        for (Training training : trainings) {
            Trainer trainer = trainerRepository.findById(training.getTrainer().getId()).orElse(null);
            if (trainer != null) {
                TrainerResponse trainerResponse = new TrainerResponse();
                trainerResponse.setUsername(trainer.getUser().getUsername());
                trainerResponse.setFirstName(trainer.getUser().getFirstName());
                trainerResponse.setLastName(trainer.getUser().getLastName());
                trainerResponse.setSpecialization(training.getTrainingType().getTrainingType().toString());
                updatedTrainers.add(trainerResponse);
            }
        }

        log.info("Updated trainer list retrieved for trainee: {}", traineeUsername);

        return updatedTrainers;
    }

}
