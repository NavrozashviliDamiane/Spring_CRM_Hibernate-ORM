package org.damiane.service;

import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.entity.User;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;




    @Override
    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findByUserUsername(username);
    }

    @Override
    public void changeTrainerPassword(Long trainerId, String newPassword) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(trainerId);
        trainerOptional.ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userService.saveUser(user);
            }
        });
    }

    @Override
    public boolean matchTrainerCredentials(String username, String password) {
        Trainer trainer = trainerRepository.findByUserUsername(username);
        return trainer != null && trainer.getUser().getPassword().equals(password);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id).orElse(null);
    }

    @Override
    public Trainer updateTrainerProfile(String username, String firstName, String lastName, TrainingTypeValue trainingTypeValue) {
        // Retrieve the trainer entity based on the provided username
        Trainer trainer = trainerRepository.findByUserUsername(username);

        // Check if the trainer exists
        if (trainer != null) {
            User user = trainer.getUser();

            // Update the user's profile attributes if new values are provided
            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }

            if (trainingTypeValue != null) {
                // Check if the training type already exists in the database
                TrainingType trainingType = trainingTypeRepository.findByTrainingType(trainingTypeValue);
                if (trainingType == null) {
                    // If the training type doesn't exist, create a new one
                    trainingType = new TrainingType(trainingTypeValue);
                    trainingTypeRepository.save(trainingType);
                }
                // Set the training type for the trainer
                trainer.setTrainingType(trainingType);
            }

            // Save the updated user entity
            userService.saveUser(user);

            // Save the updated trainer entity
            return trainerRepository.save(trainer);
        } else {
            // Handle the case where the trainer doesn't exist
            return null;
        }
    }





    @Override
    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {

        User user = userService.createUser(firstName, lastName);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        if (trainingType.getId() == null) {
            trainingTypeRepository.save(trainingType);
        }


        return trainerRepository.save(trainer);
    }

    @Override
    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }


    // TrainerServiceImpl.java
    @Override
    public void activateTrainer(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer != null) {
            User user = trainer.getUser();
            user.setActive(true);
            userService.saveUser(user);

            trainerRepository.save(trainer);
        }
    }

    @Override
    public void deactivateTrainer(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer != null) {
            User user = trainer.getUser();
            user.setActive(false);
            userService.saveUser(user);

            trainerRepository.save(trainer);
        }
    }

    // TraineeServiceImpl.java




}
