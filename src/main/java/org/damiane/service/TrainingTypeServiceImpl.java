package org.damiane.service;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional
    public TrainingType createTrainingType(TrainingTypeValue trainingTypeValue) {
        // Check if the training type already exists

        // Create a new TrainingType instance
        TrainingType trainingType = new TrainingType(trainingTypeValue);

        // Save the new training type
        return trainingTypeRepository.save(trainingType);
    }
}
