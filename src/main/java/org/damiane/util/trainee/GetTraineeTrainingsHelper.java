package org.damiane.util.trainee;

import org.damiane.dto.TrainingDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.Training;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TraineeRepository;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetTraineeTrainingsHelper {

    private final TrainerRepository trainerRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final TrainingRepository trainingRepository;

    public GetTraineeTrainingsHelper(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingRepository = trainingRepository;
    }


    public Long getTrainerId(String trainerName) {
        if (trainerName != null) {
            Trainer trainer = trainerRepository.findByUserUsername(trainerName);
            if (trainer != null) {
                return trainer.getId();
            }
        }
        return null;
    }

    public Long getTrainingTypeId(String trainingTypeName) {
        if (trainingTypeName != null) {
            TrainingType trainingType = trainingTypeRepository.findByTrainingType(TrainingTypeValue.valueOf(trainingTypeName));
            if (trainingType != null) {
                return trainingType.getId();
            }
        }
        return null;
    }

    public List<Training> constructQuery(Long traineeId, Date fromDate, Date toDate, Long trainerId, Long trainingTypeId) {
        if (fromDate != null && toDate != null) {
            if (trainerId != null && trainingTypeId != null) {
                return trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                        traineeId, fromDate, toDate, trainerId, trainingTypeId);
            } else if (trainerId != null) {
                return trainingRepository.findByTrainerIdAndTrainingDateBetweenAndTraineeId(
                        traineeId, fromDate, toDate, trainerId);
            } else if (trainingTypeId != null) {
                return trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainingTypeId(
                        traineeId, fromDate, toDate, trainingTypeId);
            } else {
                return trainingRepository.findByTraineeIdAndTrainingDateBetween(
                        traineeId, fromDate, toDate);
            }
        } else {
            return trainingRepository.findByTraineeId(traineeId);
        }
    }

    public List<TrainingDTO> mapToTrainingDTO(List<Training> trainings) {
        return trainings.stream()
                .map(training -> {
                    TrainingDTO trainingDTO = new TrainingDTO();
                    trainingDTO.setTrainingName(training.getTrainingName());
                    trainingDTO.setTrainingDate(training.getTrainingDate());
                    trainingDTO.setTrainingType(training.getTrainingType().getTrainingType().toString());
                    trainingDTO.setTrainingDuration(training.getTrainingDuration());
                    trainingDTO.setTrainerName(training.getTrainer().getUser().getFirstName() + " " +
                            training.getTrainer().getUser().getLastName());
                    return trainingDTO;
                })
                .collect(Collectors.toList());
    }
}