package org.damiane.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainerRegistrationResponse {
    private String username;
    private String password;
}

