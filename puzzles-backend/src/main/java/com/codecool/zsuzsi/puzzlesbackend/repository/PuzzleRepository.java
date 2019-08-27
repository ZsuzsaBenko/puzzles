package com.codecool.zsuzsi.puzzlesbackend.repository;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {

    List<Puzzle> findAllByOrderByDateTimeDesc();

    List<Puzzle> findAllByCategory(Category category);

    @Query("SELECT p FROM Puzzle p WHERE p NOT IN :solved AND p.category = :category")
    List<Puzzle> findUnsolved(List<Puzzle> solved, Category category);

    List<Puzzle> findAllByOrderByTitleAsc();

    List<Puzzle> findAllByOrderByTitleDesc();

    List<Puzzle> findAllByOrderByLevelAsc();

    List<Puzzle> findAllByOrderByLevelDesc();

    List<Puzzle> findAllByOrderByRatingAsc();

    List<Puzzle> findAllByOrderByRatingDesc();
}
