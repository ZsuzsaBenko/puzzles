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

    private final SolutionRepository solutionRepository;
    private final PuzzleRepository puzzleRepository;
    private final MemberRepository memberRepository;

    public Solution saveSolution(Solution solution) {
        Puzzle solvedPuzzle = puzzleRepository.findById(solution.getPuzzle().getId()).orElse(null);
        Member member = memberRepository.findByEmail(solution.getMember().getEmail()).orElse(null);
        solution.setPuzzle(solvedPuzzle);
        solution.setMember(member);
        solutionRepository.save(solution);

        this.updateRating(solvedPuzzle);
        this.updateLevel(solvedPuzzle);

        return null;
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
                .map(time -> time > 300 ? 2 : time > 120 ? 1 : 0)
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
        return solvedPuzzle;
    }
}
