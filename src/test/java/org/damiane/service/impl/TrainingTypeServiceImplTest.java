package org.damiane.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.damiane.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainingType_ValidTrainingTypeValue_ReturnsTrainingType() {
        TrainingTypeValue trainingTypeValue = TrainingTypeValue.CARDIO;
        TrainingType trainingType = new TrainingType(trainingTypeValue);

        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(trainingType);

        TrainingType createdTrainingType = trainingTypeService.createTrainingType(trainingTypeValue);

        assertNotNull(createdTrainingType);
        assertEquals(trainingType, createdTrainingType);
        verify(trainingTypeRepository, times(1)).save(any(TrainingType.class));
    }
}
