package org.damiane.controller;

import org.damiane.dto.training.TrainingRequest;
import org.damiane.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void addTraining_ValidRequest_ReturnsOk() {
        TrainingRequest request = new TrainingRequest();
        request.setTraineeUsername("trainee");
        request.setTrainerUsername("trainer");
        request.setTrainingName("training");
        request.setTrainingDate(new Date());
        request.setTrainingDuration(60);
        request.setPassword("password");

        ResponseEntity<Void> response = trainingController.addTraining(request);

        verify(trainingService).createTraining(
                request.getTraineeUsername(),
                request.getTrainerUsername(),
                request.getTrainingName(),
                request.getTrainingDate(),
                request.getTrainingDuration(),
                request.getPassword()
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
