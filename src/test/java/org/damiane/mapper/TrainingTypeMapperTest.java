package org.damiane.mapper;

import org.damiane.dto.training.TrainingTypeDTO;
import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingTypeMapperTest {

    private final TrainingTypeMapper mapper = new TrainingTypeMapper();

    @Test
    void mapToTrainingTypeDTO_TrainingType_ReturnsCorrectTrainingTypeDTO() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingType(TrainingTypeValue.CARDIO);

        TrainingTypeDTO dto = mapper.mapToTrainingTypeDTO(trainingType);

        assertEquals("CARDIO", dto.getTrainingType());
        assertEquals(1L, dto.getTrainingTypeId());
    }
}
