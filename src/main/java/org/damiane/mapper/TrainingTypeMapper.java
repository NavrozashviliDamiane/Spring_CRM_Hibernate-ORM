package org.damiane.mapper;

import org.damiane.dto.training.TrainingTypeDTO;
import org.damiane.entity.TrainingType;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {

    public TrainingTypeDTO mapToTrainingTypeDTO(TrainingType trainingType) {
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
        trainingTypeDTO.setTrainingType(trainingType.getTrainingType().toString());
        trainingTypeDTO.setTrainingTypeId(trainingType.getId());
        return trainingTypeDTO;
    }
}

