package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.PuzzleNotFoundException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolutionService {

    private static final int EASY_SCORE = 10;
    private static final int MEDIUM_SCORE = 20;
    private static final int DIFFICULT_SCORE = 50;
    private static final int MEDIUM_TIME_LIMIT = 120;
    private static final int DIFFICULT_TIME_LIMIT = 300;
    private final SolutionRepository solutionRepository;
    private final PuzzleRepository puzzleRepository;
    private final MemberRepository memberRepository;


    public List<Solution> getAllSolutionsByMember(Member member) {
        log.info("All solutions by member " + member.getEmail() + " requested");
        return solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    public List<Solution> getAllSolutionsByMember(Long id) {
        Optional<Member> requestedMember = memberRepository.findById(id);
        if (requestedMember.isEmpty()) throw new MemberNotFoundException();

        Member member = requestedMember.get();
        log.info("All solutions by member " + member.getEmail() + " requested");

        return solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    public Solution saveSolution(Solution solution, Member member) {
        Optional<Puzzle> puzzle = puzzleRepository.findById(solution.getPuzzle().getId());

        if (puzzle.isEmpty()) throw new PuzzleNotFoundException();

        Puzzle solvedPuzzle = puzzle.get();
        solution.setPuzzle(solvedPuzzle);
        solution.setMember(member);
        solutionRepository.save(solution);

        log.info("Member " + member.getEmail() + " saved solution for puzzle " + solvedPuzzle.getId() +
                "; seconds: " + solution.getSeconds() + ", rating: " + solution.getRating());

        this.updateRating(solvedPuzzle);
        this.updateScore(member, solvedPuzzle);
        this.updateLevel(solvedPuzzle);

        return solution;
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
                .mapToInt(time -> time > DIFFICULT_TIME_LIMIT ? 2 : time > MEDIUM_TIME_LIMIT ? 1 : 0)
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

    private void updateScore(Member member, Puzzle solvedPuzzle) {
        int prevScore = member.getScore();
        int maxScore = 0;
        if (solvedPuzzle.getLevel().equals(Level.EASY)) {
            maxScore = prevScore + EASY_SCORE;
        } else if (solvedPuzzle.getLevel().equals(Level.MEDIUM)) {
            maxScore = prevScore + MEDIUM_SCORE;
        } else if (solvedPuzzle.getLevel().equals(Level.DIFFICULT)) {
            maxScore = prevScore + DIFFICULT_SCORE;
        }

        member.setScore(maxScore);
        memberRepository.save(member);

        log.info("Member " + member.getEmail() + "'s previous score: " + prevScore +
                ", new score: " + member.getScore());
    }
}
