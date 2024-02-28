package org.damiane.repository;

import org.damiane.entity.Training;
import org.damiane.entity.TrainingTypeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTraineeId(Long traineeId);

    List<Training> findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
            Long traineeId, Date fromDate, Date toDate, Long trainerId, Long trainingTypeId);

    List<Training> findByTrainerIdAndTrainingDateBetweenAndTraineeId(
            Long traineeId, Date fromDate, Date toDate, Long trainerId);

}
