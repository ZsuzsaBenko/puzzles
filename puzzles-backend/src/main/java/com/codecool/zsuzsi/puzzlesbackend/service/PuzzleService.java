package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final SolutionRepository solutionRepository;


    public Puzzle getById(Long id) {
        return puzzleRepository.findById(id).orElse(null);
    }

    public List<Puzzle> getAllPuzzles() {
        return puzzleRepository.findAllByOrderByDateTimeDesc();
    }

    public List<Puzzle> getAllPuzzlesByCategory(Category category) {
        return puzzleRepository.findAllByCategory(category);
    }

    public List<Puzzle> getUnsolvedPuzzleFromEachCategory(Member member) {
        List<Puzzle> solvedPuzzles = this.getSolvedPuzzles(member);

        List<Puzzle> unsolvedPuzzles = new ArrayList<>();
        for (Category category : Category.values()) {
            unsolvedPuzzles.add(puzzleRepository.findUnsolved(solvedPuzzles, category).get(0));
        }
        return unsolvedPuzzles;
    }

    private List<Puzzle> getSolvedPuzzles(Member member) {
        List<Solution> solutionsByMember = solutionRepository.findAllByMember(member);
        List<Puzzle> solvedPuzzles = new ArrayList<>();
        for (Solution solution : solutionsByMember) {
            solvedPuzzles.add(solution.getPuzzle());
        }
        return solvedPuzzles;
    }
}
