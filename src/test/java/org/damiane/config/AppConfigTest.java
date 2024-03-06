package org.damiane.config;

import org.damiane.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AppConfigTest {

    @InjectMocks
    private AppConfig appConfig;

    @Test
    public void testAppConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService testBean = context.getBean(TraineeService.class);

        assertNotNull(testBean);
    }
}