package org.damiane.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    String username;

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    String password;
}
