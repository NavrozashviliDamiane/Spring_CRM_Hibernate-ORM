package org.damiane.controller;

import org.damiane.dto.TrainerRegistrationRequest;
import org.damiane.dto.TrainerRegistrationResponse;
import org.damiane.entity.Trainer;
import org.damiane.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<TrainerRegistrationResponse> registerTrainer(@RequestBody TrainerRegistrationRequest request) {
        Trainer trainer = trainerService.createTrainer(request);
        TrainerRegistrationResponse response = new TrainerRegistrationResponse(trainer.getUser().getUsername(), trainer.getUser().getPassword());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

