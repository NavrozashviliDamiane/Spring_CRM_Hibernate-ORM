package org.damiane.controller;

import org.damiane.dto.TraineeProfileDTO;
import org.damiane.dto.TraineeRegistrationDTO;
import org.damiane.dto.UserCredentialsDTO;
import org.damiane.entity.Trainee;
import org.damiane.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationDTO registrationDTO) {
        Trainee createdTrainee = traineeService.createTrainee(
                registrationDTO.getFirstName(),
                registrationDTO.getLastName(),
                registrationDTO.getDateOfBirth(),
                registrationDTO.getAddress()
        );

        UserCredentialsDTO credentials = new UserCredentialsDTO();
        credentials.setUsername(createdTrainee.getUser().getUsername());
        credentials.setPassword(createdTrainee.getUser().getPassword());
        return new ResponseEntity<>(credentials, HttpStatus.CREATED);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getTraineeProfile(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        TraineeProfileDTO profileDTO = traineeService.getTraineeProfile(username, password);
        if (profileDTO == null) {
            return new ResponseEntity<>("Trainee not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }
}
