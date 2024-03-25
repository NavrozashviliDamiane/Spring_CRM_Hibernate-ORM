package org.damiane.dto.trainer;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingsRequestDTO {
    private String username;
    private Date periodFrom;
    private Date periodTo;
    private String traineeName;
}