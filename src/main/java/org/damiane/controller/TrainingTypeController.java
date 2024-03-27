package org.damiane.controller;

import org.damiane.dto.training.TrainingTypeDTO;
import org.damiane.entity.TrainingType;
import org.damiane.mapper.TrainingTypeMapper;
import org.damiane.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/training-types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        List<TrainingTypeDTO> trainingTypeDTOs = trainingTypes.stream()
                .map(trainingTypeMapper::mapToTrainingTypeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainingTypeDTOs);
    }
}

