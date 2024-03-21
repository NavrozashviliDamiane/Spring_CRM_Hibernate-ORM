package org.damiane.util;

import org.damiane.dto.TraineeDTO;
import org.damiane.dto.trainer.TrainerProfileDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.User;
import org.damiane.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.damiane.entity.Training;
import org.damiane.entity.Trainee;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainerProfileDtoCreator {

    @Autowired
    private TrainingRepository trainingRepository;

    public TrainerProfileDTO createTrainerProfileDTO(Trainer trainer) {
        TrainerProfileDTO profileDTO = new TrainerProfileDTO();
        User user = trainer.getUser();
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setIsActive(user.isActive());

        TrainingType trainingType = trainer.getTrainingType();
        if (trainingType != null) {
            profileDTO.setSpecialization(trainingType.getTrainingType().toString());
        }

        List<Training> trainings = trainingRepository.findByTrainerId(trainer.getId());
        List<TraineeDTO> trainees = trainings.stream()
                .map(training -> {
                    Trainee trainee = training.getTrainee();
                    TraineeDTO traineeDTO = new TraineeDTO();
                    User traineeUser = trainee.getUser();
                    traineeDTO.setUsername(traineeUser.getUsername());
                    traineeDTO.setFirstName(traineeUser.getFirstName());
                    traineeDTO.setLastName(traineeUser.getLastName());
                    return traineeDTO;
                })
                .collect(Collectors.toList());

        profileDTO.setTrainees(trainees);
        return profileDTO;
    }
}

