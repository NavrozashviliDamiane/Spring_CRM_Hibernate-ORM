package org.damiane.service;

import org.damiane.dto.*;
import org.damiane.dto.trainer.TrainerProfileDTO;
import org.damiane.dto.trainer.TrainerRegistrationRequest;
import org.damiane.entity.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface TrainerService {

    Trainer getTrainerByUsername(String username, String password);

    void changeTrainerPassword(Long trainerId, String username, String password, String newPassword);

    List<Trainer> getAllTrainers(String username, String password);

    TrainerProfileDTO getTrainerProfile(String username, String password);

    Trainer getTrainerById(Long id, String username, String password);





    @Transactional
    Trainer createTrainer(TrainerRegistrationRequest request);

    void deleteTrainer(Long id, String username, String password);

    void activateTrainer(Long trainerId, String username, String password);

    void deactivateTrainer(Long trainerId, String username, String password);


    @Transactional
    void updateTrainerStatus(String username, boolean isActive);

    List<TrainerDTO> findUnassignedActiveTrainersByTraineeUsername(String traineeUsername, String password);

    @Transactional
    TrainerProfileDTO updateTrainerProfile(TrainerUpdateDTO trainerUpdateDTO);

    @Transactional(readOnly = true)
    List<TrainerTrainingResponseDTO> getTrainerTrainings(TrainerTrainingsRequestDTO request);
}
