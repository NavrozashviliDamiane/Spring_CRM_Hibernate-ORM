package org.damiane.service;

import org.damiane.entity.Trainee;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TraineeService {
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);

    boolean matchTraineeCredentials(String username, String password);

    Trainee getTraineeByUsername(String username);



    Trainee updateTraineeProfile(String username, String firstName, String lastName, Date dateOfBirth, String address);

    void changeTraineePassword(Long traineeId, String newPassword);

    Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address);

    void deleteTrainee(Long id);



    void deactivateTrainee(Long traineeId);

    void activateTrainee(Long traineeId);




    // TraineeServiceImpl.java

}
