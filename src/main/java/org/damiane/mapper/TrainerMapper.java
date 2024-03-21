package org.damiane.mapper;

import org.damiane.dto.TrainerDTO;
import org.damiane.entity.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public TrainerDTO convertToTrainerDTO(Trainer trainer) {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setUsername(trainer.getUser().getUsername());
        trainerDTO.setFirstName(trainer.getUser().getFirstName());
        trainerDTO.setLastName(trainer.getUser().getLastName());
        trainerDTO.setSpecialization(trainer.getTrainingType().getTrainingType().toString());
        return trainerDTO;
    }
}

