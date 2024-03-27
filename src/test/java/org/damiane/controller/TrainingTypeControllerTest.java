package org.damiane.controller;

import org.damiane.dto.training.TrainingTypeDTO;
import org.damiane.entity.TrainingType;
import org.damiane.mapper.TrainingTypeMapper;
import org.damiane.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void GivenTrainingTypesExist_WhenGettingTrainingTypes_ThenReturnTrainingTypeDTOList() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType());
        trainingTypes.add(new TrainingType());

        List<TrainingTypeDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new TrainingTypeDTO());
        expectedDTOs.add(new TrainingTypeDTO());

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);
        when(trainingTypeMapper.mapToTrainingTypeDTO(any(TrainingType.class))).thenReturn(expectedDTOs.get(0), expectedDTOs.get(1));

        ResponseEntity<List<TrainingTypeDTO>> responseEntity = trainingTypeController.getTrainingTypes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDTOs.size(), responseEntity.getBody().size());
        verify(trainingTypeService, times(1)).getAllTrainingTypes();
        verify(trainingTypeMapper, times(2)).mapToTrainingTypeDTO(any(TrainingType.class));
    }

    @Test
    void GivenNoTrainingTypesExist_WhenGettingTrainingTypes_ThenReturnEmptyList() {
        List<TrainingType> trainingTypes = new ArrayList<>();

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        ResponseEntity<List<TrainingTypeDTO>> responseEntity = trainingTypeController.getTrainingTypes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
        verify(trainingTypeService, times(1)).getAllTrainingTypes();
        verifyNoInteractions(trainingTypeMapper);
    }
}
