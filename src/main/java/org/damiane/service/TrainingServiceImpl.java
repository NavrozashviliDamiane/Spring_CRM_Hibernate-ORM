package org.damiane.service;

import org.damiane.entity.Training;
import org.damiane.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingRepository.findById(id).orElse(null);
    }

    @Override
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
}
