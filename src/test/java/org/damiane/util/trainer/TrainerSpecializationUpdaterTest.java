package org.damiane.util.trainer;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.dto.trainer.TrainerUpdateDTO;
import org.damiane.entity.Trainer;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerSpecializationUpdaterTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerSpecializationUpdater trainerSpecializationUpdater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateSpecialization() {
        Trainer trainer = new Trainer();
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        trainerUpdateDTO.setSpecialization("YOGA");
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingType(TrainingTypeValue.YOGA);

        when(trainingTypeRepository.findByTrainingType(TrainingTypeValue.YOGA)).thenReturn(trainingType);

        trainerSpecializationUpdater.updateSpecialization(trainer, trainerUpdateDTO);

        assertEquals(trainingType, trainer.getTrainingType());
    }

    @Test
    public void testUpdateSpecializationWithNullSpecialization() {
        Trainer trainer = new Trainer();
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();

        trainerSpecializationUpdater.updateSpecialization(trainer, trainerUpdateDTO);

        assertNull(trainer.getTrainingType());
    }

    @Test
    public void testUpdateSpecializationWithUnknownSpecialization() {
        Trainer trainer = new Trainer();
        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO();
        trainerUpdateDTO.setSpecialization("YOGA");

        when(trainingTypeRepository.findByTrainingType(TrainingTypeValue.YOGA)).thenReturn(null);

        trainerSpecializationUpdater.updateSpecialization(trainer, trainerUpdateDTO);

        assertNull(trainer.getTrainingType());
    }
}
