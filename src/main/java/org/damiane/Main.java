package org.damiane;

import org.damiane.config.AppConfig;
import org.damiane.entity.*;
import org.damiane.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


    }
}
