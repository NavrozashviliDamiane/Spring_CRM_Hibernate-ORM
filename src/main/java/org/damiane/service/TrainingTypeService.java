package org.damiane.service;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrainingTypeService {
    TrainingType createTrainingType(TrainingTypeValue trainingTypeValue);

    @Transactional(readOnly = true)
    List<TrainingType> getAllTrainingTypes();
}
