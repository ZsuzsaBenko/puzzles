package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.*;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.method.P;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackageClasses = {SolutionService.class})
@Import(JwtTokenServices.class)
class SolutionServiceTest {

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PuzzleRepository puzzleRepository;

    @MockBean
    private SolutionRepository solutionRepository;

    @Autowired
    private SolutionService solutionService;

    @Test
    public void testGetAllSolutionsByMember() {
        Member member = Member.builder().email("email@email.hu").build();
        Solution solution1 = Solution.builder().member(member).submissionTime(LocalDateTime.now()).build();
        Solution solution2 = Solution.builder().member(member)
                .submissionTime(LocalDateTime.of(2019, 9, 9, 10, 10)).build();
        Solution solution3 = Solution.builder().member(member)
                .submissionTime(LocalDateTime.of(2019, 9, 8, 10, 10)).build();
        List<Solution> expected = Arrays.asList(solution1, solution2, solution3);

        when(solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(expected);

        List<Solution> result = solutionService.getAllSolutionsByMember(member);

        assertIterableEquals(expected, result);
        verify(solutionRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testSaveSolution() {
        Level initialLevel = Level.EASY;
        Level expectedLevel = Level.MEDIUM;
        List<Integer> solutionTimes = Arrays.asList(60, 150, 300);
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).title("puzzle").level(initialLevel).build();

        int expectedScore = 30;
        Member member = Member.builder().email("email@email.hu").score(0).build();

        int solutionSeconds = 400;
        int newRating = 5;
        double expectedRating = 4.4;
        Solution solution = Solution.builder().puzzle(Puzzle.builder().id(1L).build())
                .seconds(solutionSeconds).rating(newRating).build();

        when(puzzleRepository.findById(solution.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(solutionRepository.getRatingAverage(solvedPuzzle)).thenReturn(expectedRating);
        when(solutionRepository.getSolutionTimes(solvedPuzzle)).thenReturn(solutionTimes);
        when(solutionRepository.findAllByMember(member)).thenReturn(Arrays.asList(
                Solution.builder().id(1L).puzzle(Puzzle.builder().level(Level.EASY).build()).build(),
                Solution.builder().id(1L).puzzle(Puzzle.builder().level(Level.MEDIUM).build()).build()
        ));

        Solution expected = Solution.builder().puzzle(Puzzle.builder().id(1L).build())
                .seconds(solutionSeconds).rating(newRating).member(member).puzzle(solvedPuzzle).build();

        Solution result = solutionService.saveSolution(solution, member);
        System.out.println(solvedPuzzle);

        assertEquals(expected, result);
        assertEquals(expectedRating, solvedPuzzle.getRating());
        assertEquals(expectedLevel, solvedPuzzle.getLevel());
        assertEquals(expectedScore, (int) member.getScore());

        verify(puzzleRepository).findById(solution.getPuzzle().getId());
        verify(solutionRepository).getRatingAverage(solvedPuzzle);
        verify(solutionRepository).getSolutionTimes(solvedPuzzle);
        verify(solutionRepository).findAllByMember(member);
    }
}

/*

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

    private Puzzle updateRating(Puzzle solvedPuzzle) {
        double prevRating = solvedPuzzle.getRating();
        double newRating = solutionRepository.getRatingAverage(solvedPuzzle);
        solvedPuzzle.setRating(newRating);
        puzzleRepository.save(solvedPuzzle);

        log.info("Previous rating: " + prevRating + ", new rating: " + solvedPuzzle.getRating());

        return solvedPuzzle;
    }

    private Puzzle updateLevel(Puzzle solvedPuzzle) {
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

        return solvedPuzzle;
    }

    private Member updateScore(Member member) {
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

        return member;
    }

 */