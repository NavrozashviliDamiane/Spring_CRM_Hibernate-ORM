package org.damiane.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@OpenAPIDefinition
@ComponentScan(basePackages = {"org.damiane"})
public class AppConfig {

}
