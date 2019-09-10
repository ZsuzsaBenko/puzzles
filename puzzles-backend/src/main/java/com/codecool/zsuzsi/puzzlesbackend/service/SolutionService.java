package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolutionService {

    private static final int EASY_SCORE = 10;
    private static final int MEDIUM_SCORE = 20;
    private static final int HARD_SCORE = 50;
    private static final int MEDIUM_TIME_LIMIT = 120;
    private static final int HARD_TIME_LIMIT = 300;
    private final SolutionRepository solutionRepository;
    private final PuzzleRepository puzzleRepository;
    private final MemberRepository memberRepository;


    public List<Solution> getAllSolutionsByMember(Member member) {
        log.info("All solutions by member " + member.getEmail() + " requested");
        return solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    public Solution saveSolution(Solution solution, Member member) {
        Puzzle solvedPuzzle = puzzleRepository.findById(solution.getPuzzle().getId()).orElse(null);

        if (solvedPuzzle != null) {
            solution.setPuzzle(solvedPuzzle);
            solution.setMember(member);
            solutionRepository.save(solution);

            log.info("Member " + member.getEmail() + " saved solution for puzzle " + solvedPuzzle.getId() +
                    "; seconds: " + solution.getSeconds() + ", rating: " + solution.getRating());

            this.updateRating(solvedPuzzle);
            this.updateScore(member);
            this.updateLevel(solvedPuzzle);

            return solution;
        }
        return null;
    }

    private void updateRating(Puzzle solvedPuzzle) {
        double prevRating = solvedPuzzle.getRating();
        Double newRating = solutionRepository.getRatingAverage(solvedPuzzle);

        if (newRating != null) {
            solvedPuzzle.setRating(newRating);
        }
        puzzleRepository.save(solvedPuzzle);

        log.info("Previous rating: " + prevRating + ", new rating: " + solvedPuzzle.getRating());
    }

    private void updateLevel(Puzzle solvedPuzzle) {
        Level prevLevel = solvedPuzzle.getLevel();
        List<Integer> solutionTimes = solutionRepository.getSolutionTimes(solvedPuzzle);
        double levelAverage = solutionTimes.stream()
                .map(time -> time > HARD_TIME_LIMIT ? 2 : time > MEDIUM_TIME_LIMIT ? 1 : 0)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        if (levelAverage <= 0.5) {
            solvedPuzzle.setLevel(Level.EASY);
        } else if (levelAverage <= 1.5) {
            solvedPuzzle.setLevel(Level.MEDIUM);
        } else {
            solvedPuzzle.setLevel(Level.DIFFICULT);
        }
        puzzleRepository.save(solvedPuzzle);

        log.info("Previous level: " + prevLevel + ", new level: " + solvedPuzzle.getLevel());
    }

    private void updateScore(Member member) {
        int prevScore = member.getScore();
        List<Solution> solutions = solutionRepository.findAllByMember(member);
        int score = solutions.stream()
                .map(solution -> solution.getPuzzle().getLevel().ordinal() + 1)
                .map(num -> num == 1 ? EASY_SCORE : num == 2 ? MEDIUM_SCORE : HARD_SCORE)
                .mapToInt(Integer::intValue)
                .sum();
        member.setScore(score);
        memberRepository.save(member);

        log.info("Member " + member.getEmail() + "'s previous score: " + prevScore +
                ", new score: " + member.getScore());
    }
}
