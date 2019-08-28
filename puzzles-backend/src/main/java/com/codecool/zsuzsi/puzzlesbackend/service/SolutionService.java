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


    public List<Solution> getAllSolutionsOfMember(Member member) {
        Member loggedInMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
        return solutionRepository.findAllByMember(loggedInMember);
    }

    public Solution saveSolution(Solution solution) {
        Puzzle solvedPuzzle = puzzleRepository.findById(solution.getPuzzle().getId()).orElse(null);
        Member member = memberRepository.findByEmail(solution.getMember().getEmail()).orElse(null);
        solution.setPuzzle(solvedPuzzle);
        solution.setMember(member);
        solutionRepository.save(solution);

        this.updateRating(solvedPuzzle);
        this.updateLevel(solvedPuzzle);
        this.updateScore(member);

        return solution;
    }

    private Puzzle updateRating(Puzzle solvedPuzzle) {
        double newRating = solutionRepository.getRatingAverage(solvedPuzzle);
        solvedPuzzle.setRating(newRating);
        puzzleRepository.save(solvedPuzzle);
        return solvedPuzzle;
    }

    private Puzzle updateLevel(Puzzle solvedPuzzle) {
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
        return solvedPuzzle;
    }

    private Member updateScore(Member member) {
        List<Solution> solutions = solutionRepository.findAllByMember(member);
        int score = solutions.stream()
                .map(solution -> solution.getPuzzle().getLevel().ordinal() + 1)
                .map(num -> num == 1 ? EASY_SCORE : num == 2 ? MEDIUM_SCORE : HARD_SCORE)
                .mapToInt(Integer::intValue)
                .sum();
        member.setScore(score);
        memberRepository.save(member);
        return member;
    }
}
