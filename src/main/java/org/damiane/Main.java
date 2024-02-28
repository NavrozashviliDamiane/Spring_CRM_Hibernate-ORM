package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.repository.UserRepository;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Create an application context with AppConfig
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Obtain the required beans from the Spring context
        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);



        // New profile information
        // Demo activation and deactivation of a trainer


        // Demo activation and deactivation of a trainee
        Long traineeId = 1L;
        Long trainerId = 2L;
        System.out.println("Deactivating trainee...");
//        traineeService.activateTrainee(traineeId);
        traineeService.deactivateTrainee(traineeId);
        trainerService.activateTrainer(trainerId);


        System.out.println("Trainee deactivated successfully.");

        //TODO Implement activate/deactivate logic correctly bassed on this example ebove

        // Close the Spring context
        context.close();
    }
}
