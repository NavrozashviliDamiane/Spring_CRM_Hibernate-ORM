package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.repository.UserRepository;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        AuthenticateService authenticateService = context.getBean(AuthenticateService.class);

        TrainingType trainingType = new TrainingType(TrainingTypeValue.CROSSFIT);



        String traineeUsername = "Olivia.Harris";

        String password = "Cj8Xpg3D0j";


        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.set(1997, Calendar.OCTOBER, 30);
        Date dateOfBirth = dobCalendar.getTime();



        traineeService.updateTraineeProfile(traineeUsername, "ChangedOlivia", password,
                "ChangedOliviasLastname", dateOfBirth, "Tbilisi");

        context.close();
    }

}
