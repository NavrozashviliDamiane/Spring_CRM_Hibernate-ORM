package org.damiane.util;

import org.damiane.dto.trainer.TrainerUpdateDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerSpecializationUpdater {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    public void updateSpecialization(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO) {
        String specialization = trainerUpdateDTO.getSpecialization();
        if (specialization != null) {
            TrainingType trainingType = trainingTypeRepository.findByTrainingType(TrainingTypeValue.valueOf(specialization.toUpperCase()));
            if (trainingType != null) {
                trainer.setTrainingType(trainingType);
            }
        }
    }
}

