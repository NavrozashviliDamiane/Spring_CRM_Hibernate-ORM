package org.damiane.service;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;

public interface TrainingTypeService {
    TrainingType createTrainingType(TrainingTypeValue trainingTypeValue);
}
