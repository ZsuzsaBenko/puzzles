package com.codecool.zsuzsi.puzzlesbackend.repository;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

    List<Solution> findAllByMember(Member member);

    List<Solution> findAllByMemberOrderBySubmissionTimeDesc(Member member);

    @Query("SELECT AVG(s.rating) FROM Solution s WHERE s.puzzle = :puzzle AND s.rating > 0")
    Double getRatingAverage(Puzzle puzzle);

    @Query("SELECT s.seconds FROM Solution s WHERE s.puzzle = :puzzle")
    List<Integer> getSolutionTimes(Puzzle puzzle);

    @Query("SELECT s.member FROM Solution s WHERE s.puzzle = :puzzle")
    List<Member> getMembersWhoSolvedPuzzle(Puzzle puzzle);
}
