package org.damiane.mapper;

import org.damiane.dto.TrainerTrainingResponseDTO;
import org.damiane.entity.Trainee;
import org.damiane.entity.Training;
import org.damiane.entity.User;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;

@Component
public class TrainerTrainingMapper {

    public TrainerTrainingResponseDTO mapTrainingToResponseDTO(Training training) {
        Trainee trainee = training.getTrainee();
        User user = trainee.getUser();
        String traineeUsername = user.getUsername();

        return new TrainerTrainingResponseDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().getTrainingType().toString(),
                training.getTrainingDuration(),
                traineeUsername
        );
    }
}

