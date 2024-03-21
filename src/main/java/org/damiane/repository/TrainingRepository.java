package org.damiane.repository;

import org.damiane.entity.Trainee;
import org.damiane.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTraineeId(Long traineeId);

    List<Training> findByTrainerId(Long trainerId);


    List<Training> findByTraineeIdAndTrainingDateBetweenAndTrainerIdAndTrainingTypeId(
            Long traineeId, Date fromDate, Date toDate, Long trainerId, Long trainingTypeId);

    List<Training> findByTrainerIdAndTrainingDateBetweenAndTraineeId(
            Long traineeId, Date fromDate, Date toDate, Long trainerId);


    List<Training> findByTraineeIdAndTrainingDateBetweenAndTrainingTypeId(Long traineeId, Date fromDate, Date toDate, Long trainingTypeId);

    List<Training> findByTraineeIdAndTrainingDateBetween(Long traineeId, Date fromDate, Date toDate);

    List<Training> findByTrainerUserUsernameAndTrainingDateBetween(String username, Date periodFrom, Date periodTo);

    List<Training> findByTrainerUserUsername(String username);
}
