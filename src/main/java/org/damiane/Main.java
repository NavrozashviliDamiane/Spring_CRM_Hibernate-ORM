package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.repository.UserRepository;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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



        Date fromDate = new Date(124, 0, 1); // January 1, 2024
        Date toDate = new Date(124, 11, 12);

        // Simulate inputs
        String traineeName = "Alice.Anderson";
        String username = "John.Doe1";

        List<Training> trainings = trainingService.getTrainingsByTrainerUsernameAndCriteria(username, fromDate, toDate, traineeName);

        // Process the results
        if (trainings.isEmpty()) {
            System.out.println("No trainings found for trainee: " + username);
        } else {
            System.out.println("Trainings for trainee " + username + ":");
            for (Training training : trainings) {
                System.out.println(training); // Assuming Training class has a proper toString() method
            }
        }


        context.close();
    }


}
