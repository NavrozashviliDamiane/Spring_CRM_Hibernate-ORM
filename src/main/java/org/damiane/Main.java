package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        AuthenticateService authenticateService = context.getBean(AuthenticateService.class);

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CROSSFIT);

        String traineeUsername = "Alice.Anderson";

        String password = "QSBfMXlJTN";

        trainerService.findUnassignedTrainersByTraineeUsername(traineeUsername, password).forEach(System.out::println);
    }
}
