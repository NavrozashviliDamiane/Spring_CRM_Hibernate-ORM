package org.damiane.service;

import jakarta.persistence.EntityManager;
import org.damiane.entity.*;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private EntityManager entityManager;


    @Override
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return traineeRepository.findById(id).orElse(null);
    }

    @Override
    public boolean matchTraineeCredentials(String username, String password) {
        Trainee trainee = traineeRepository.findByUserUsername(username);
        return trainee != null && trainee.getUser().getPassword().equals(password);
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.findByUserUsername(username);
    }

    @Override
    public Trainee updateTraineeProfile(String username, String firstName, String lastName, Date dateOfBirth, String address) {
        Trainee trainee = traineeRepository.findByUserUsername(username);

        if (trainee != null) {
            User user = trainee.getUser();

            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }

            trainee.setDateOfBirth(dateOfBirth);
            trainee.setAddress(address);

            userService.saveUser(user);

            return traineeRepository.save(trainee);
        } else {
            return null;
        }
    }

    @Override
    public void changeTraineePassword(Long traineeId, String newPassword) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(traineeId);
        traineeOptional.ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userService.saveUser(user);
            }
        });
    }

    @Override
    public Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address) {

        User user = userService.createUser(firstName, lastName);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);


        return traineeRepository.save(trainee);
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeRepository.deleteById(id);
    }


    @Override
    public void activateTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId).orElse(null);
        if (trainee != null) {
            User user = trainee.getUser();
            user.setActive(true);
            userService.saveUser(user);

            traineeRepository.save(trainee);
        }
    }

    @Override
    public void deactivateTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId).orElse(null);
        if (trainee != null) {
            User user = trainee.getUser();
            user.setActive(false);
            userService.saveUser(user);

            traineeRepository.save(trainee);
        }
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        trainingService.updateTrainingForTrainee(username);

        Trainee trainee = traineeRepository.findByUserUsername(username);

        if (trainee != null) {
            traineeRepository.delete(trainee);
        }
}



}
