package org.damiane.service.impl;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TrainingTypeRepository;
import org.damiane.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional
    public TrainingType createTrainingType(TrainingTypeValue trainingTypeValue) {

        TrainingType trainingType = new TrainingType(trainingTypeValue);

        return trainingTypeRepository.save(trainingType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
