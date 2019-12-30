package com.codecool.zsuzsi.puzzlesbackend.repository;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {

    List<Puzzle> findAllByOrderBySubmissionTimeDesc();

    List<Puzzle> findAllByCategory(Category category);

    List<Puzzle> findAllByMember(Member member);

    List<Puzzle> findAllByMemberOrderBySubmissionTimeDesc(Member member);

    @Query("SELECT p FROM Puzzle p WHERE p NOT IN :solved AND p.category = :category")
    List<Puzzle> findUnsolved(List<Puzzle> solved, Category category);

    Optional<Puzzle> findFirstByCategory(Category category);

    List<Puzzle> findAllByOrderByTitleAsc();

    List<Puzzle> findAllByOrderByTitleDesc();

    List<Puzzle> findAllByOrderByLevelAsc();

    List<Puzzle> findAllByOrderByLevelDesc();

    List<Puzzle> findAllByOrderByRatingAsc();

    List<Puzzle> findAllByOrderByRatingDesc();

    List<Puzzle> findAllByCategoryOrderByTitleAsc(Category category);

    List<Puzzle> findAllByCategoryOrderByTitleDesc(Category category);

    List<Puzzle> findAllByCategoryOrderByLevelAsc(Category category);

    List<Puzzle> findAllByCategoryOrderByLevelDesc(Category category);

    List<Puzzle> findAllByCategoryOrderByRatingAsc(Category category);

    List<Puzzle> findAllByCategoryOrderByRatingDesc(Category category);
}
