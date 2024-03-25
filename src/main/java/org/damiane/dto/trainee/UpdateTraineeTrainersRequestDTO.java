package org.damiane.dto.trainee;
import lombok.*;
import org.damiane.dto.trainer.TrainerDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeTrainersRequestDTO {
    private String traineeUsername;
    private List<TrainerDTO> trainersList;
}
