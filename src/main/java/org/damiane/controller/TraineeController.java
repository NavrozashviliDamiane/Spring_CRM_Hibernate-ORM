package org.damiane.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.damiane.dto.trainee.TraineeProfileDTO;
import org.damiane.dto.trainee.TraineeRegistrationDTO;
import org.damiane.dto.trainee.TraineeUpdateDTO;
import org.damiane.dto.trainer.TrainerResponse;
import org.damiane.dto.training.TrainingDTO;
import org.damiane.dto.user.UserCredentialsDTO;
import org.damiane.entity.Trainee;
import org.damiane.exception.AuthenticationException;
import org.damiane.service.AuthenticateService;
import org.damiane.service.TraineeService;
import org.damiane.mapper.TraineeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@Tag(name = "User", description = "User-related operations")
@RestController
@Slf4j
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    private final AuthenticateService authenticateService;

    private final TraineeMapper traineeMapper;


    @Autowired
    public TraineeController(TraineeService traineeService,
                             AuthenticateService authenticateService,
                             TraineeMapper traineeMapper) {
        this.traineeService = traineeService;
        this.authenticateService = authenticateService;
        this.traineeMapper = traineeMapper;
    }

    @PostMapping("/register")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserCredentialsDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationDTO registrationDTO) {

        log.info("REST call made to /api/trainees/register endpoint. Request: {}", registrationDTO);


        try {
            Trainee createdTrainee = traineeService.createTrainee(
                    registrationDTO.getFirstName(),
                    registrationDTO.getLastName(),
                    registrationDTO.getDateOfBirth(),
                    registrationDTO.getAddress()
            );

            UserCredentialsDTO credentials = new UserCredentialsDTO();
            credentials.setUsername(createdTrainee.getUser().getUsername());
            credentials.setPassword(createdTrainee.getUser().getPassword());

            log.info("Successfully created trainee. Response: {}", credentials);

            return new ResponseEntity<>(credentials, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("\"Error occurred while processing /api/trainees/register endpoint.\", e");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getTraineeProfile(@RequestParam String username, @RequestParam String password) {

        log.info("REST call made to /api/trainees/get-profile endpoint. Request: {} {}", username, password);

        try {
            boolean isAuthenticated = authenticateService.matchUserCredentials(username, password);
            log.info("User Authenticated Successfully");

            if (isAuthenticated) {
                TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username);
                return ResponseEntity.ok(profileDTO);
            } else {
                log.error("Authentication failed for user: {}", username);
                throw new AuthenticationException("Invalid username or password");
            }
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            log.info("\"Error occurred while processing /api/trainees/register endpoint.\", e");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }



    @PutMapping("/update-profile")
    public ResponseEntity<?> updateTraineeProfile(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @Valid @RequestBody TraineeUpdateDTO updateDTO) {

        log.info("REST call made to /api/trainees/update-profile endpoint. Request: {} {} {}", username, password, updateDTO);

        try {

            if (!authenticateService.matchUserCredentials(username, password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }

            Trainee updatedTrainee = traineeService.updateTraineeProfile(
                    username,
                    updateDTO.getFirstName(),
                    password,
                    updateDTO.getLastName(),
                    updateDTO.getDateOfBirth(),
                    updateDTO.getAddress(),
                    updateDTO.isActive()
            );

            return updatedTrainee != null ?
                    ResponseEntity.ok(traineeMapper.mapTraineeToDTO(updatedTrainee)) :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.info("\"Error occurred while processing /api/trainees/update-profile endpoint.\", e");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    @DeleteMapping("/delete-profile")
    public ResponseEntity<String> deleteTraineeProfile(@RequestParam String username, @RequestParam String password) {

        log.info("REST call made to /api/trainees/delete-profile endpoint. Request: {} {}", username, password);

        try {

        if (!authenticateService.matchUserCredentials(username, password)) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        traineeService.deleteTraineeByUsername(username);

        return new ResponseEntity<>("Trainee profile deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            log.info("\"Error occurred while processing /api/trainees/delete-profile endpoint.\", e");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }

    @PatchMapping("/update-trainee-status")
    public ResponseEntity<String> updateTrainerStatus(@RequestParam String username,
                                                      @RequestParam boolean isActive) {
        traineeService.updateTraineeStatus(username, isActive);
        return ResponseEntity.ok("Trainer status updated successfully!");
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingDTO>> getTraineeTrainingsList(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingTypeName,
            @RequestParam String password) {

        try {
            log.info("REST call made to /api/trainees/trainings endpoint. Request: {} {}", username, password);

            List<TrainingDTO> trainings = traineeService.getTraineeTrainingsList(username, password, fromDate, toDate, trainerName, trainingTypeName);

            if (trainings == null || trainings.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(trainings);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing /api/trainees/trainings endpoint.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new TrainingDTO("Error occurred while processing the request. Please try again later.", null, null, null, null)));
        }
    }



    @PutMapping("/{traineeUsername}/trainers")
    public ResponseEntity<List<TrainerResponse>> updateTraineeTrainerList(
            @PathVariable String traineeUsername,
            @RequestBody List<String> trainerUsernames) {
        log.info("Received request to update trainer list for trainee: {}", traineeUsername);

        List<TrainerResponse> updatedTrainers = traineeService.updateTraineeTrainerList(traineeUsername, trainerUsernames);
        if (updatedTrainers != null) {
            log.info("Trainer list updated successfully for trainee: {}", traineeUsername);
            return ResponseEntity.ok(updatedTrainers);
        } else {
            log.warn("Trainee not found with username: {}", traineeUsername);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
