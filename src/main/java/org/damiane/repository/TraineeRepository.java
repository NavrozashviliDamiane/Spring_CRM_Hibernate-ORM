package org.damiane.repository;


import org.damiane.entity.Trainee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @EntityGraph(attributePaths = "user")
    Trainee findByUserUsername(String username);

}

