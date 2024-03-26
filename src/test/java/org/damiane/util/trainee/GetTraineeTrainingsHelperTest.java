package org.damiane.util.trainee;


import org.damiane.dto.training.TrainingDTO;
import org.damiane.entity.*;
import org.damiane.repository.TrainerRepository;
import org.damiane.repository.TrainingRepository;
import org.damiane.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetTraineeTrainingsHelperTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private GetTraineeTrainingsHelper trainingsHelper;

    @Test
    public void testGetTrainerId() {
        String trainerName = "JohnDoe";
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        when(trainerRepository.findByUserUsername(trainerName)).thenReturn(trainer);

        Long trainerId = trainingsHelper.getTrainerId(trainerName);

        assert trainerId != null;
        assert trainerId.equals(1L);
    }


    @Test
    public void testConstructQuery() {
        Long traineeId = 1L;
        Date fromDate = new Date();
        Date toDate = new Date();
        Long trainerId = 2L;
        Long trainingTypeId = 3L;
        when(trainingRepository.findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
                traineeId, fromDate, toDate, trainerId, trainingTypeId)).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingsHelper.constructQuery(traineeId, fromDate, toDate, trainerId, trainingTypeId);

        assert trainings != null;
        assert trainings.isEmpty();
    }

    @Test
    public void testMapToTrainingDTO() {
        Training training = new Training();
        training.setTrainingName("Training1");
        training.setTrainingType(null);
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        trainer.setUser(user);
        training.setTrainer(trainer);

        List<TrainingDTO> trainingDTOs = trainingsHelper.mapToTrainingDTO(Collections.singletonList(training));

        assert trainingDTOs != null;
        assert trainingDTOs.size() == 1;
        TrainingDTO trainingDTO = trainingDTOs.get(0);
        assert trainingDTO.getTrainingName().equals("Training1");
        assert trainingDTO.getTrainingType().equals("Unknown");
        assert trainingDTO.getTrainerName().equals("John Doe");
    }


    @Test
    public void testGetTrainingTypeId() {
        String trainingTypeName = "CARDIO";
        TrainingTypeValue validTrainingTypeValue = TrainingTypeValue.CARDIO;
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        when(trainingTypeRepository.findByTrainingType(validTrainingTypeValue)).thenReturn(trainingType);

        Long trainingTypeId = trainingsHelper.getTrainingTypeId(trainingTypeName);

        assert trainingTypeId != null;
        assert trainingTypeId.equals(1L);
    }

}
