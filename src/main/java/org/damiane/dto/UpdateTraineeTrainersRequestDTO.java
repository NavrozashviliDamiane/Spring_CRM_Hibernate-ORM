package org.damiane.dto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeTrainersRequestDTO {
    private String traineeUsername;
    private List<TrainerDTO> trainersList;
}
