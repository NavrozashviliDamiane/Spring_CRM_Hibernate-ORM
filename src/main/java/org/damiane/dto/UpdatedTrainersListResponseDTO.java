package org.damiane.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedTrainersListResponseDTO {
    private List<TrainerDTO> trainersList;
}
