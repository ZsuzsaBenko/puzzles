package com.codecool.zsuzsi.puzzlesbackend.repository;

import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
