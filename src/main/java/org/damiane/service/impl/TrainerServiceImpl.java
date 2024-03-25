package org.damiane.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.trainee.TraineeDTO;
import org.damiane.dto.trainer.*;
import org.damiane.entity.*;
import org.damiane.repository.*;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TrainerService;
import org.damiane.service.UserService;
import org.damiane.mapper.TrainerMapper;
import org.damiane.mapper.TrainerTrainingMapper;
import org.damiane.util.TrainerProfileDtoCreator;
import org.damiane.util.TrainerSpecializationUpdater;
import org.damiane.util.UserUpdateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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

    private final TrainerSpecializationUpdater specializationUpdater;

    private final UserUpdateHelper userUpdateHelper;

    private final TrainerTrainingMapper trainerTrainingMapper;

    private final TrainerMapper trainerMapper;

    private final TrainerProfileDtoCreator profileDtoCreator;


    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService,
                              TrainingTypeRepository trainingTypeRepository,
                              TraineeRepository traineeRepository, TrainingRepository trainingRepository,
                              AuthenticateService authenticateService, Training training,
                              TrainerMapper trainerMapper, TrainerTrainingMapper trainerTrainingMapper,
                              UserRepository userRepository, UserUpdateHelper userUpdateHelper,
                              TrainerSpecializationUpdater specializationUpdater,
                              TrainerProfileDtoCreator profileDtoCreator) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.authenticateService = authenticateService;
        this.trainerMapper = trainerMapper;
        this.trainerTrainingMapper = trainerTrainingMapper;
        this.userUpdateHelper = userUpdateHelper;
        this.specializationUpdater = specializationUpdater;
        this.profileDtoCreator = profileDtoCreator;
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
    @Transactional
    public TrainerProfileDTO getTrainerProfile(String username, String password) {

        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for getting trainer profile. Transaction ID: {}", transactionId);

        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        Trainer trainer = trainerRepository.findByUserUsername(username);
        if (trainer == null) {
            return null;
        }

        TrainerProfileDTO profileDTO = new TrainerProfileDTO();
        User user = trainer.getUser();
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setIsActive(user.isActive());
        TrainingType trainingType = trainer.getTrainingType();
        if (trainingType != null) {
            profileDTO.setSpecialization(trainingType.getTrainingType().toString());
        }
        log.info("Transaction finished to retrieve user from trainer entity. Transaction ID: {}", transactionId);


        List<Training> trainings = trainingRepository.findByTrainerId(trainer.getId());
        List<TraineeDTO> trainees = trainings.stream()
                .map(training -> {
                    Trainee trainee = training.getTrainee();
                    TraineeDTO traineeDTO = new TraineeDTO();
                    traineeDTO.setUsername(trainee.getUser().getUsername());
                    traineeDTO.setFirstName(trainee.getUser().getFirstName());
                    traineeDTO.setLastName(trainee.getUser().getLastName());
                    return traineeDTO;
                })
                .collect(Collectors.toList());

        profileDTO.setTrainees(trainees);

        log.info("Transaction successful to retrieve trainer profile. Transaction ID: {}", transactionId);
        return profileDTO;
    }

    @Override
    public Trainer getTrainerById(Long id, String username, String password) {
        authenticateService.matchUserCredentials(username, password);
        log.info("User Authenticated Successfully");

        return trainerRepository.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public Trainer createTrainer(TrainerRegistrationRequest request) {

        String transactionId = UUID.randomUUID().toString();
        log.info("Transaction started for trainee creation. Transaction ID: {}", transactionId);

        try {
            User user = userService.createUser(request.getFirstName(), request.getLastName());

            TrainingTypeValue trainingTypeValue = TrainingTypeValue.valueOf(request.getSpecialization().toUpperCase());
            TrainingType trainingType = new TrainingType(trainingTypeValue);

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

        } catch (Exception e) {
            log.info("Error occurred while creating trainer. Transaction ID: {}", transactionId, e);
            throw e;
        }
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

    @Override
    @Transactional
    public void updateTrainerStatus(String username, boolean isActive) {
        Trainer trainer = trainerRepository.findByUserUsername(username);
        if (trainer != null) {
            User user = trainer.getUser();
            user.setActive(isActive);
            userService.saveUser(user);

            log.info("Trainer status updated successfully!");
        } else {
            log.error("Trainer not found with username: " + username);
        }
    }


    @Override
    public List<TrainerDTO> findUnassignedActiveTrainersByTraineeUsername(String traineeUsername, String password) {

        authenticateService.matchUserCredentials(traineeUsername, password);
        log.info("User Authenticated Successfully");

        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername);
        List<Training> trainingsWithTrainee = trainingRepository.findByTraineeId(trainee.getId());

        log.info("Unassigned Active Trainer found successfully");
        return trainerRepository.findAll().stream()
                .filter(trainer -> trainingsWithTrainee.stream()
                        .noneMatch(training -> Objects.equals(training.getTrainer(), trainer)))
                .map(trainerMapper::convertToTrainerDTO)
                .collect(Collectors.toList());

    }


    @Override
    public TrainerProfileDTO updateTrainerProfile(TrainerUpdateDTO trainerUpdateDTO) {
        String username = trainerUpdateDTO.getUsername();
        String password = trainerUpdateDTO.getPassword();

        authenticateService.matchUserCredentials(username, password);
        Trainer trainer = trainerRepository.findByUserUsername(username);
        if (trainer == null) {
            return null; // Trainer not found
        }

        User user = trainer.getUser();
        userUpdateHelper.updateUser(user, trainerUpdateDTO);
        specializationUpdater.updateSpecialization(trainer, trainerUpdateDTO);

        return profileDtoCreator.createTrainerProfileDTO(trainer);
    }


    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingResponseDTO> getTrainerTrainings(TrainerTrainingsRequestDTO request) {
        List<Training> trainings;

        trainings = trainingRepository.findByTrainerUserUsernameAndTrainingDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
                request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTraineeName());

        return trainings.stream()
                .map(trainerTrainingMapper::mapTrainingToResponseDTO)
                .collect(Collectors.toList());
    }



}
