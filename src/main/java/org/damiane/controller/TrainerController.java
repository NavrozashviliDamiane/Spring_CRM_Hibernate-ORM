package org.damiane.controller;

import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.trainer.*;
import org.damiane.entity.Trainer;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TrainerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    private final AuthenticateService authenticateService;

    public TrainerController(TrainerService trainerService, AuthenticateService authenticateService) {
        this.trainerService = trainerService;
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    public ResponseEntity<TrainerRegistrationResponse> registerTrainer(@RequestBody TrainerRegistrationRequest request) {
        log.info("REST call made to /api/trainers/register endpoint. Request: {}", request);

        try {
            Trainer trainer = trainerService.createTrainer(request);
            TrainerRegistrationResponse response = new TrainerRegistrationResponse(
                    trainer.getUser().getUsername(),
                    trainer.getUser().getPassword());

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            log.info("Error occurred while processing /api/trainers/register endpoint: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getTrainerProfile(
            @RequestParam String username,
            @RequestParam String password
    ) {

        log.info("REST call made to /api/trainers/get-profile endpoint. Request: {} {}", username, password);
        if (!authenticateService.matchUserCredentials(username, password)) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        TrainerProfileDTO trainerProfile = trainerService.getTrainerProfile(username, password);
        if (trainerProfile != null) {

            return ResponseEntity.ok(trainerProfile);
        } else {
            log.info("\"Error occurred while processing /api/trainers/get-profile endpoint.\", e");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateTrainerProfile(@RequestBody TrainerUpdateDTO trainerUpdateDTO) {

        log.info("REST call made to /api/trainers/update-profile endpoint. Request: {}", trainerUpdateDTO);

        TrainerProfileDTO updatedProfile = trainerService.updateTrainerProfile(trainerUpdateDTO);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            log.info("\"Error occurred while processing /api/trainers/update-profile endpoint.\", e");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }
    }

    @PatchMapping("/update-trainer-status")
    public ResponseEntity<String> updateTrainerStatus(@RequestParam String username,
                                                      @RequestParam boolean isActive) {
        trainerService.updateTrainerStatus(username, isActive);
        return ResponseEntity.ok("Trainer status updated successfully!");
    }

    @GetMapping("/unassigned-active")
    public ResponseEntity<List<TrainerDTO>> getUnassignedActiveTrainersByTraineeUsername(
            @RequestParam String traineeUsername,
            @RequestParam String password
    ) {

        log.info("REST call made to /api/trainers/unassigned-active endpoint. Request: {} {}", traineeUsername, password);
        if (!authenticateService.matchUserCredentials(traineeUsername, password)) {
            return new ResponseEntity("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        List<TrainerDTO> unassignedActiveTrainers = trainerService.findUnassignedActiveTrainersByTraineeUsername(traineeUsername, password);
        return ResponseEntity.ok(unassignedActiveTrainers);

    }

    @GetMapping("/trainer/trainings")
    public ResponseEntity<List<TrainerTrainingResponseDTO>> getTrainerTrainings(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(required = false) String traineeName
    ) {

        log.info("REST call made to /api/trainers/trainings endpoint. Request: {} {}", username, password);
        if (!authenticateService.matchUserCredentials(username, password)) {
            return new ResponseEntity("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        try {
            TrainerTrainingsRequestDTO request = new TrainerTrainingsRequestDTO();
            request.setUsername(username);
            request.setPeriodFrom(periodFrom);
            request.setPeriodTo(periodTo);
            request.setTraineeName(traineeName);

            List<TrainerTrainingResponseDTO> trainings = trainerService.getTrainerTrainings(request);
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            log.info("Error occurred while processing /api/trainers/trainings endpoint:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
