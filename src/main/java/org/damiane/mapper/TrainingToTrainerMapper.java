package org.damiane.mapper;

import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.Training;
import org.damiane.entity.User;
import org.springframework.stereotype.Component;


@Component
public class TrainingToTrainerMapper {

    public TrainerDTO mapTrainingToTrainerDTO(Training training) {
        TrainerDTO trainerDTO = new TrainerDTO();
        Trainer trainer = training.getTrainer();
        User trainerUser = trainer.getUser();
        trainerDTO.setUsername(trainerUser.getUsername());
        trainerDTO.setFirstName(trainerUser.getFirstName());
        trainerDTO.setLastName(trainerUser.getLastName());
        trainerDTO.setSpecialization(training.getTrainingType().getTrainingType().toString());
        return trainerDTO;
    }

}
