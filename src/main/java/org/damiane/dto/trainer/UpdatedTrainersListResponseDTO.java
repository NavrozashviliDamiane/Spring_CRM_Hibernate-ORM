package org.damiane.dto.trainer;

import lombok.*;
import org.damiane.dto.trainer.TrainerDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedTrainersListResponseDTO {
    private List<TrainerDTO> trainersList;
}
