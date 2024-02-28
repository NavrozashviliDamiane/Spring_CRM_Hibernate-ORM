package org.damiane.config;

import org.damiane.repository.TraineeRepository;
import org.damiane.service.TraineeService;
import org.damiane.service.TraineeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.damiane"})
@ComponentScan(basePackages = "org.damiane.repository")
@ComponentScan(basePackages = "org.damiane.service")
@ComponentScan(basePackages = "org.damiane.entity")
@ComponentScan(basePackages = "org.damiane.config")
public class AppConfig {





}


