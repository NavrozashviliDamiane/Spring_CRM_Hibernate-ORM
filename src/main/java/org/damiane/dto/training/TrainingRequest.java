package org.damiane.dto.training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.damiane.entity.TrainingTypeValue;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Date trainingDate;
    private Integer trainingDuration;
    private String password;

}

