package org.damiane.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {
    private String trainingName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date trainingDate;
    private String trainingType;
    private Integer trainingDuration;
    private String trainerName;
}
