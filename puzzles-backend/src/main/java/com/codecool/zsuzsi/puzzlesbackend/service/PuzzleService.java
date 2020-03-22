package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.PuzzleNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.*;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PuzzleService {

    private static final int HELPER_LETTER_NUMBER_MEDIUM = 5;
    private static final int HELPER_LETTER_NUMBER_HARD = 3;
    private static final int EASY_SCORE = 10;
    private static final int MEDIUM_SCORE = 20;
    private static final int DIFFICULT_SCORE = 50;
    private final CipherMaker cipherMaker;
    private final PuzzleRepository puzzleRepository;
    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;


    public Puzzle getById(Long id) {
        log.info("Puzzle with id " + id + " requested");

        Optional<Puzzle> puzzle = puzzleRepository.findById(id);
        if (puzzle.isEmpty()) {
            throw new PuzzleNotFoundException();
        }
        return puzzle.get();
    }

    public List<Puzzle> getAllPuzzles() {
        log.info("All puzzles requested");
        return puzzleRepository.findAllByOrderBySubmissionTimeDesc();
    }

    public List<Puzzle> getAllPuzzlesByCategory(Category category) {
        log.info("Puzzles with category " + category + " requested");
        return puzzleRepository.findAllByCategory(category);
    }

    public List<Puzzle> getAllPuzzlesByMember(Member member) {
        log.info("Puzzles of  " + member.getUsername() + " (" + member.getEmail() + ") requested");
        return puzzleRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    public List<Puzzle> getAllPuzzlesByMember(Long id) {
        Optional<Member> requestedMember = memberRepository.findById(id);
        if (requestedMember.isEmpty()) throw new MemberNotFoundException();

        Member member = requestedMember.get();
        log.info("Puzzles of  " + member.getUsername() + " (" + member.getEmail() + ") requested");

        return puzzleRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    public List<Puzzle> getUnsolvedPuzzleFromEachCategory(Member member) {
        List<Puzzle> solvedPuzzles = this.getSolvedPuzzles(member);
        List<Puzzle> unsolvedPuzzles = new ArrayList<>();

        log.info("Unsolved random puzzles requested for member " + member.getEmail() + ", " +
                "number of already solved puzzles: " + solvedPuzzles.size());

        if (!solvedPuzzles.isEmpty()) {
            for (Category category : Category.values()) {
                List<Puzzle> notYetSolved = puzzleRepository.findUnsolved(solvedPuzzles, category);
                if (!notYetSolved.isEmpty()) {
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
        log.info("Sorted puzzles requested. Sorting criteria: " + criteria);
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
        log.info("Sorted puzzles of category " + category + " requested. Sorting criteria: " + criteria);
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
        log.info("Member " + member.getEmail() + " added new puzzle: " + puzzle.getTitle() +
                ", category: " + puzzle.getCategory() + ", level: " + puzzle.getLevel());
        puzzle.setMember(member);

        if (Category.CIPHER.equals(puzzle.getCategory())) {
            buildCipherPuzzle(puzzle);
        }
        puzzleRepository.save(puzzle);

        return puzzle;
    }

    public Puzzle updatePuzzle(Long id, Puzzle updatedPuzzle) {
        log.info("Update for puzzle with id '" + id + "' requested with data: '" + updatedPuzzle.getTitle() +
                "' as title, '"  + updatedPuzzle.getPuzzleItem() + "' as puzzle item, '" + updatedPuzzle.getInstruction() +
                "' as instruction and '" + updatedPuzzle.getAnswer() + "' as answer.");

        Optional<Puzzle> puzzleToBeUpdated = puzzleRepository.findById(id);

        if (puzzleToBeUpdated.isEmpty()) throw new PuzzleNotFoundException();

        Puzzle puzzle = puzzleToBeUpdated.get();

        if (Category.CIPHER.equals(puzzle.getCategory())) {
            puzzle.setTitle(updatedPuzzle.getTitle());
        } else if (Category.PICTURE_PUZZLE.equals(puzzle.getCategory())) {
            puzzle.setTitle(updatedPuzzle.getTitle());
            puzzle.setInstruction(updatedPuzzle.getInstruction());
            puzzle.setAnswer(updatedPuzzle.getAnswer());
        } else {
            puzzle.setTitle(updatedPuzzle.getTitle());
            puzzle.setInstruction(updatedPuzzle.getInstruction());
            puzzle.setPuzzleItem(updatedPuzzle.getPuzzleItem());
            puzzle.setAnswer(updatedPuzzle.getAnswer());
        }
        puzzleRepository.save(puzzle);

        return puzzle;
    }

    public void deletePuzzle(Long id) {
        log.info("Deletion of puzzle with id " + id + " requested.");
        Optional<Puzzle> puzzleToBeDeleted = puzzleRepository.findById(id);

        if (puzzleToBeDeleted.isEmpty()) throw new PuzzleNotFoundException();

        Puzzle puzzle = puzzleToBeDeleted.get();

        decreaseScore(puzzle);
        puzzleRepository.delete(puzzle);
    }

    public Boolean checkAnswer(Long id, String answer) {
        Optional<Puzzle> puzzle = puzzleRepository.findById(id);
        if (puzzle.isEmpty()) throw new PuzzleNotFoundException();

        String correctAnswer = puzzle.get().getAnswer().trim().toLowerCase();
        answer = answer.trim().toLowerCase();

        return correctAnswer.equals(answer);
    }

    private void decreaseScore(Puzzle puzzle) {
        List<Member> membersWhoSolvedPuzzle = solutionRepository.getMembersWhoSolvedPuzzle(puzzle);
        int scoreValue = Level.EASY.equals(puzzle.getLevel()) ? EASY_SCORE :
                Level.MEDIUM.equals(puzzle.getLevel()) ? MEDIUM_SCORE : DIFFICULT_SCORE;

        membersWhoSolvedPuzzle.forEach(member -> {
            member.setScore(member.getScore() - scoreValue);
            memberRepository.save(member);
        });
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
        if (Level.EASY.equals(puzzle.getLevel())) {
            buildShiftCipherPuzzle(puzzle);
        }
        else {
            buildRandomCipherPuzzle(puzzle);
        }
        log.info("Encrypted text created: " + puzzle.getPuzzleItem() + ", solution: " + puzzle.getAnswer());
    }

    private void buildShiftCipherPuzzle(Puzzle puzzle) {
        Random random = new Random();
        int alphabetLength = 35;
        int randomNumber = random.nextInt(alphabetLength) + 1;

        String instruction = "Fejtsd meg a titkosírást! Az abc minden betűjét \"arréb toltuk\" valamennyivel.";
        puzzle.setInstruction(instruction);

        String puzzleItem = cipherMaker.createShiftCipher(puzzle.getAnswer(), randomNumber);
        puzzle.setPuzzleItem(puzzleItem);
    }

    private void buildRandomCipherPuzzle(Puzzle puzzle) {
        Map<String, Map<String, String>> puzzleWithHelp;
        if (Level.MEDIUM.equals(puzzle.getLevel())) {
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

        String instruction = "Fejtsd meg a titkosírást! Az abc minden betűje egy másik betűnek felel meg, " +
                "teljesen véletlenszerűen. Egy kis segítség: " + help + ". Az egyenlőségjel bal oldalán " +
                "az eredeti betű áll, a jobb oldalon a titkosírásban használt megfelelője.";
        puzzle.setInstruction(instruction);
    }
}
