package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.*;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PuzzleService {

    private static final int HELPER_LETTER_NUMBER_MEDIUM = 5;
    private static final int HELPER_LETTER_NUMBER_HARD = 3;
    private final CipherMaker cipherMaker;
    private final PuzzleRepository puzzleRepository;
    private final SolutionRepository solutionRepository;


    public Puzzle getById(Long id) {
        return puzzleRepository.findById(id).orElse(null);
    }

    public List<Puzzle> getAllPuzzles() {
        return puzzleRepository.findAllByOrderBySubmissionTimeDesc();
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

        if (solvedPuzzles.size() > 0) {
            for (Category category : Category.values()) {
                List<Puzzle> notYetSolved = puzzleRepository.findUnsolved(solvedPuzzles, category);
                if (notYetSolved.size() > 0) {
                    unsolvedPuzzles.add(notYetSolved.get(0));
                }
            }
        } else {
            for (Category category : Category.values()) {
                unsolvedPuzzles.add(puzzleRepository.findFirstByCategory(category).orElse(null));
            }
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
                return puzzleRepository.findAllByOrderBySubmissionTimeDesc();
        }
    }

    public List<Puzzle> getSortedPuzzles(Category category, String criteria) {
        switch (criteria) {
            case "titleASC":
                return puzzleRepository.findAllByCategoryOrderByTitleAsc(category);
            case "titleDESC":
                return puzzleRepository.findAllByCategoryOrderByTitleDesc(category);
            case "levelASC":
                return puzzleRepository.findAllByCategoryOrderByLevelAsc(category);
            case "levelDESC":
                return puzzleRepository.findAllByCategoryOrderByLevelDesc(category);
            case "ratingASC":
                return puzzleRepository.findAllByCategoryOrderByRatingAsc(category);
            case "ratingDESC":
                return puzzleRepository.findAllByCategoryOrderByRatingDesc(category);
            default:
                return puzzleRepository.findAllByOrderBySubmissionTimeDesc();
        }
    }

    public Puzzle addNewPuzzle(Puzzle puzzle, Member member) {
        puzzle.setMember(member);

        if (puzzle.getCategory().equals(Category.CIPHER)) {
            buildCipherPuzzle(puzzle);
        }

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

    private void buildCipherPuzzle(Puzzle puzzle) {
        if (puzzle.getLevel().equals(Level.EASY)) {
            puzzle.setInstruction("Fejtsd meg a titkosírást! Az abc minden betűjét \"arréb toltuk\" valamennyivel.");
            String puzzleItem = cipherMaker.createShiftCipher(puzzle.getAnswer(), 5);
            puzzle.setPuzzleItem(puzzleItem);
        }
        else {
            Map<String, Map<String, String>> puzzleWithHelp;
            if (puzzle.getLevel().equals(Level.MEDIUM)) {
                puzzleWithHelp = cipherMaker.createRandomCipher(puzzle.getAnswer(),
                        HELPER_LETTER_NUMBER_MEDIUM);
            } else {
                puzzleWithHelp = cipherMaker.createRandomCipher(puzzle.getAnswer(),
                        HELPER_LETTER_NUMBER_HARD);
            }

            String puzzleItem = "";
            Map<String, String> help = new HashMap<>();

            for (String item : puzzleWithHelp.keySet()) {
                puzzleItem = item;
                help = puzzleWithHelp.get(item);
            }

            puzzle.setPuzzleItem(puzzleItem);
            puzzle.setInstruction("Fejtsd meg a titkosírást! Az abc minden betűje egy másik betűnek felel meg, " +
                    "teljesen véletlenszerűen. Egy kis segítség: " + help + ". Az egyenlőségjel bal oldalán " +
                    "az eredeti betű áll, a jobb oldalon a titkosírásban használt megfelelője.");
        }
    }
}
