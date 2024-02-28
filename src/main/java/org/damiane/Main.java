package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.repository.UserRepository;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Create an application context with AppConfig
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Obtain the required beans from the Spring context
        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);



        String usernameToDelete = "Alice.Clark";

        // Delete the trainee profile by username
        traineeService.deleteTraineeByUsername(usernameToDelete);

        // Verify if the trainee profile has been deleted
        if (traineeService.getTraineeByUsername(usernameToDelete) == null) {
            System.out.println("Trainee profile with username " + usernameToDelete + " deleted successfully.");
        } else {
            System.out.println("Failed to delete trainee profile with username " + usernameToDelete);
        }






        // Close the Spring context
        context.close();
    }


}
