package org.damiane.dto;

import lombok.Data;

@Data
public class TrainerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String specialization;
}
