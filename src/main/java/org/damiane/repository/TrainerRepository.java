package org.damiane.repository;

import org.damiane.entity.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @EntityGraph(attributePaths = "user")
    Trainer findByUserUsername(String username);
}
