package org.damiane.service;

import org.damiane.dto.TraineeProfileDTO;
import org.damiane.entity.Trainee;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TraineeService {

    List<Trainee> getAllTrainees(String username, String password);

    TraineeProfileDTO getTraineeProfile(String username, String password);

    Trainee getTraineeById(Long id, String username, String password);

    Trainee getTraineeByUsername(String username, String password);

    Trainee updateTraineeProfile(String username, String firstName, String password, String lastName,
                                 Date dateOfBirth, String address);

    void changeTraineePassword(Long traineeId, String username, String password, String newPassword);

    Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address);

    void activateTrainee(Long traineeId, String username, String password);

    void deactivateTrainee(Long traineeId, String username, String password);

    void deleteTraineeByUsername(String username, String password);
}
