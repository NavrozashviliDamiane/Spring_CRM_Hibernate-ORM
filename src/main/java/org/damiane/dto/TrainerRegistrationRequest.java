package org.damiane.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainerRegistrationRequest {

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    private String lastName;

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    private String specialization;
}