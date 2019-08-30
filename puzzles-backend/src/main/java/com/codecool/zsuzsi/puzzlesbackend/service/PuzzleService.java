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

    public List<Puzzle> getAllPuzzlesByMember(Member member) {
        return puzzleRepository.findAllByMember(member);
    }

    public List<Puzzle> getUnsolvedPuzzleFromEachCategory(Member member) {
        List<Puzzle> solvedPuzzles = this.getSolvedPuzzles(member);
        List<Puzzle> unsolvedPuzzles = new ArrayList<>();
        for (Category category : Category.values()) {
            unsolvedPuzzles.add(puzzleRepository.findUnsolved(solvedPuzzles, category).get(0));
        }
        return unsolvedPuzzles;
    }

    public List<Puzzle> getSortedPuzzles(String criteria) {
        switch (criteria) {
            case "titleASC":
                return puzzleRepository.findAllByOrderByTitleAsc();
            case "titleDESC":
                return puzzleRepository.findAllByOrderByTitleDesc();
            case "levelASC":
                return puzzleRepository.findAllByOrderByLevelAsc();
            case "levelDESC":
                return puzzleRepository.findAllByOrderByLevelDesc();
            case "ratingASC":
                return puzzleRepository.findAllByOrderByRatingAsc();
            case "ratingDESC":
                return puzzleRepository.findAllByOrderByRatingDesc();
            default:
                return puzzleRepository.findAllByOrderByDateTimeDesc();
        }
    }

    public Puzzle addNewPuzzle(Puzzle puzzle, Member member) {
        puzzle.setMember(member);
        return puzzleRepository.save(puzzle);
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
