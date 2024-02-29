package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.repository.UserRepository;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        AuthenticateService authenticateService = context.getBean(AuthenticateService.class);

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CARDIO);

        String traineeUsername = "Alice.Anderson";

        List<Trainer> unassignedTrainers = trainerService.findUnassignedTrainersByTraineeUsername(traineeUsername);

        System.out.println("Unassigned Trainers for Trainee with Username '" + traineeUsername + "':");
        for (Trainer trainer : unassignedTrainers) {
            System.out.println("Trainer ID: " + trainer.getId());
        }

        String username = "Alice.Smith";
        String password = "changedTraineePasswor";

        Boolean authenticationResult = authenticateService.matchUserCredentials(username, password);

        if (authenticationResult==true) {
            System.out.println("Authentication Successfull");
        } else {
            System.out.println("Authentication failed");
        }


        context.close();
    }

}
