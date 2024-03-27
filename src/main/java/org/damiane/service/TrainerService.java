package org.damiane.service;

import org.damiane.dto.trainer.*;
import org.damiane.entity.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface TrainerService {


    TrainerProfileDTO getTrainerProfile(String username, String password);

    @Transactional
    Trainer createTrainer(TrainerRegistrationRequest request);


    @Transactional
    void updateTrainerStatus(String username, boolean isActive);

    List<TrainerDTO> findUnassignedActiveTrainersByTraineeUsername(String traineeUsername, String password);

    @Transactional
    TrainerProfileDTO updateTrainerProfile(TrainerUpdateDTO trainerUpdateDTO);

    @Transactional(readOnly = true)
    List<TrainerTrainingResponseDTO> getTrainerTrainings(TrainerTrainingsRequestDTO request);
}
