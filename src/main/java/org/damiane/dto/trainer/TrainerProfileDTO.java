package org.damiane.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.damiane.dto.trainee.TraineeDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDTO {
    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Specialization name is required")
    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Status is required")
    @NotBlank(message = "Status is required")
    private boolean isActive;

    private List<TraineeDTO> trainees;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}

