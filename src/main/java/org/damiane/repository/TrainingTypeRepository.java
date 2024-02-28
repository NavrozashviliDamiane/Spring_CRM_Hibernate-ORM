package org.damiane.repository;

import org.damiane.entity.TrainingType;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    TrainingType findByTrainingType(TrainingTypeValue trainingType);

}
