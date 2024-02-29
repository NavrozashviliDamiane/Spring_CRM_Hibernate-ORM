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

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CARDIO);

        // Create a trainer
        String traineeUsername = "trainee_username";

        List<Trainer> unassignedTrainers = trainerService.findUnassignedTrainersByTraineeUsername(traineeUsername);

        System.out.println("Unassigned Trainers for Trainee with Username '" + traineeUsername + "':");
        for (Trainer trainer : unassignedTrainers) {
            System.out.println("Trainer ID: " + trainer.getId() + ", Trainer User ID: " + trainer.getUser().getId());
        }


        context.close();
    }






}
