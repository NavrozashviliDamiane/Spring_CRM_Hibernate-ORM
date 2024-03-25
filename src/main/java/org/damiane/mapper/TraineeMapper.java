package org.damiane.mapper;

import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainer.TrainerDTO;
import org.damiane.entity.Trainee;
import org.damiane.entity.Trainer;
import org.damiane.entity.Training;
import org.damiane.entity.User;
import org.damiane.repository.TrainingRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraineeMapper {

    private final TrainingRepository trainingRepository;

    public TraineeMapper(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public TraineeProfileDTO mapTraineeToDTO(Trainee trainee) {
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();
        User user = trainee.getUser();
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setDateOfBirth(trainee.getDateOfBirth());
        profileDTO.setAddress(trainee.getAddress());
        profileDTO.setActive(user.isActive());

        List<Training> trainings = trainingRepository.findByTraineeId(trainee.getId());
        List<TrainerDTO> trainers = trainings.stream()
                .map(training -> {
                    TrainerDTO trainerDTO = new TrainerDTO();
                    Trainer trainer = training.getTrainer();
                    trainerDTO.setUsername(trainer.getUser().getUsername());
                    trainerDTO.setFirstName(trainer.getUser().getFirstName());
                    trainerDTO.setLastName(trainer.getUser().getLastName());
                    trainerDTO.setSpecialization(training.getTrainingType().getTrainingType().toString());
                    return trainerDTO;
                })
                .collect(Collectors.toList());

        profileDTO.setTrainers(trainers);

        return profileDTO;
    }
}

