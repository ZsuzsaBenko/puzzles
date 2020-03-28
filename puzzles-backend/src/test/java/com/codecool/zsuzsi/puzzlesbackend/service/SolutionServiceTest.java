package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.SolutionNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackageClasses = {SolutionService.class})
@Import({JwtTokenServices.class, CipherMaker.class})
class SolutionServiceTest {

    @MockBean
    private PuzzleRepository puzzleRepository;

    @MockBean
    private SolutionRepository solutionRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private SolutionService solutionService;

    @Test
    public void testGetAllSolutionsByMember() {
        Member member = Member.builder().email("email@email.hu").build();
        List<Solution> expected = Arrays.asList(
                Solution.builder().member(member).submissionTime(LocalDateTime.now()).build(),
                Solution.builder().member(member)
                        .submissionTime(LocalDateTime.of(2019, 10, 10, 10, 10)).build(),
                Solution.builder().member(member)
                        .submissionTime(LocalDateTime.of(2019, 9, 10, 10, 10)).build());

        when(solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(expected);

        List<Solution> result = solutionService.getAllSolutionsByMember(member);

        assertIterableEquals(expected, result);
        verify(solutionRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testGetAllSolutionsByMemberAsAdmin() {
        Long id = 1L;
        Member member = Member.builder().email("email@email.hu").build();
        List<Solution> expected = Arrays.asList(
                Solution.builder().member(member).submissionTime(LocalDateTime.now()).build(),
                Solution.builder().member(member)
                        .submissionTime(LocalDateTime.of(2019, 10, 10, 10, 10)).build(),
                Solution.builder().member(member)
                        .submissionTime(LocalDateTime.of(2019, 9, 10, 10, 10)).build());

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        when(solutionRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(expected);

        List<Solution> result = solutionService.getAllSolutionsByMember(id);

        assertIterableEquals(expected, result);
        verify(memberRepository).findById(id);
        verify(solutionRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testGetAllSolutionsByMemberAsAdminWithNonexistentMember() {
        Long id = 1L;
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> solutionService.getAllSolutionsByMember(id));
    }

    @Test
    public void testSaveSolutionWhenPuzzleIsAlreadySolvedByUser() {
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).title("puzzle").build();
        Member member = Member.builder().email("email@email.hu").build();
        Solution solution = Solution.builder().puzzle(solvedPuzzle).build();

        when(puzzleRepository.findById(solution.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(solutionRepository.getMembersWhoSolvedPuzzle(solvedPuzzle)).thenReturn(List.of(member));

        Solution result = solutionService.saveSolution(solution, member);

        assertEquals(solution, result);
        verify(puzzleRepository).findById(solution.getPuzzle().getId());
        verify(solutionRepository).getMembersWhoSolvedPuzzle(solvedPuzzle);
        verifyNoMoreInteractions(puzzleRepository);
        verifyNoMoreInteractions(solutionRepository);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    public void testSaveSolution() {
        /*
        EASY_SCORE = 10;
        MEDIUM_TIME_LIMIT = 120;
        DIFFICULT_TIME_LIMIT = 300;
         */

        Level initialLevel = Level.EASY;
        Level expectedLevel = Level.MEDIUM;
        List<Integer> solutionTimes = Arrays.asList(60, 150, 300);
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).title("puzzle").level(initialLevel).build();

        int initialScore = 20;
        int expectedScore = 30;
        Member member = Member.builder().email("email@email.hu").score(initialScore).build();

        int solutionSeconds = 400;
        int newRating = 5;
        double expectedRating = 4.4;
        Solution solution = Solution.builder().puzzle(Puzzle.builder().id(1L).build())
                .seconds(solutionSeconds).rating(newRating).build();

        when(puzzleRepository.findById(solution.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(solutionRepository.getMembersWhoSolvedPuzzle(solvedPuzzle)).thenReturn(new ArrayList<>());
        when(solutionRepository.getRatingAverage(solvedPuzzle)).thenReturn(expectedRating);
        when(solutionRepository.getSolutionTimes(solvedPuzzle)).thenReturn(solutionTimes);

        Solution expected = Solution.builder().puzzle(Puzzle.builder().id(1L).build())
                .seconds(solutionSeconds).rating(newRating).member(member).puzzle(solvedPuzzle).build();

        Solution result = solutionService.saveSolution(solution, member);
        System.out.println(solvedPuzzle);

        assertEquals(expected, result);
        assertEquals(expectedRating, solvedPuzzle.getRating());
        assertEquals(expectedLevel, solvedPuzzle.getLevel());
        assertEquals(expectedScore, (int) member.getScore());

        verify(puzzleRepository).findById(solution.getPuzzle().getId());
        verify(solutionRepository).getMembersWhoSolvedPuzzle(solvedPuzzle);
        verify(solutionRepository).getRatingAverage(solvedPuzzle);
        verify(solutionRepository).getSolutionTimes(solvedPuzzle);
        verify(memberRepository).save(member);
    }

    @Test
    public void testDeleteSolutionWhenSolutionDoesNotExist() {
        Long id = 1L;
        when(solutionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SolutionNotFoundException.class, () -> solutionService.deleteSolution(id));
        verify(solutionRepository).findById(id);
    }

    @Test
    public void testDeleteSolutionWhenLevelIsChangedToMedium() {
        double initialRating = 4;
        double expectedRating = 5;
        Level initialLevel = Level.EASY;
        Level expectedLevel = Level.MEDIUM;
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).rating(initialRating).level(initialLevel).build();

        int initialScore = 100;
        int expectedScore = 90;
        Member member = Member.builder().id(1L).email("email@email.hu").score(initialScore).build();

        Long solutionId = 1L;
        Solution solutionToDelete = Solution.builder().id(solutionId).member(member).puzzle(solvedPuzzle).build();
        List<Integer> solutionTimes = Arrays.asList(150, 300);

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(solutionToDelete));
        when(puzzleRepository.findById(solutionToDelete.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(solutionRepository.getRatingAverage(solvedPuzzle)).thenReturn(expectedRating);
        when(solutionRepository.getSolutionTimes(solvedPuzzle)).thenReturn(solutionTimes);

        this.solutionService.deleteSolution(solutionId);

        assertEquals(expectedRating, solvedPuzzle.getRating());
        assertEquals(expectedScore, (int) member.getScore());
        assertEquals(expectedLevel, solvedPuzzle.getLevel());

        verify(solutionRepository).findById(solutionId);
        verify(puzzleRepository).findById(solutionToDelete.getPuzzle().getId());
        verify(solutionRepository).getRatingAverage(solvedPuzzle);
        verify(memberRepository).findById(member.getId());
        verify(solutionRepository).getSolutionTimes(solvedPuzzle);
        verify(solutionRepository).delete(solutionToDelete);
        verify(memberRepository).save(member);
        verify(puzzleRepository, times(2)).save(solvedPuzzle);

        verifyNoMoreInteractions(solutionRepository);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(puzzleRepository);
    }

    @Test
    public void testDeleteSolutionWhenLevelIsChangedToEasy() {
        double initialRating = 4;
        double expectedRating = 5;
        Level initialLevel = Level.MEDIUM;
        Level expectedLevel = Level.EASY;
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).rating(initialRating).level(initialLevel).build();

        int initialScore = 100;
        int expectedScore = 80;
        Member member = Member.builder().id(1L).email("email@email.hu").score(initialScore).build();

        Long solutionId = 1L;
        Solution solutionToDelete = Solution.builder().id(solutionId).member(member).puzzle(solvedPuzzle).build();
        List<Integer> solutionTimes = Arrays.asList(50, 120);

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(solutionToDelete));
        when(puzzleRepository.findById(solutionToDelete.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(solutionRepository.getRatingAverage(solvedPuzzle)).thenReturn(expectedRating);
        when(solutionRepository.getSolutionTimes(solvedPuzzle)).thenReturn(solutionTimes);

        this.solutionService.deleteSolution(solutionId);

        assertEquals(expectedRating, solvedPuzzle.getRating());
        assertEquals(expectedScore, (int) member.getScore());
        assertEquals(expectedLevel, solvedPuzzle.getLevel());

        verify(solutionRepository).findById(solutionId);
        verify(puzzleRepository).findById(solutionToDelete.getPuzzle().getId());
        verify(solutionRepository).getRatingAverage(solvedPuzzle);
        verify(memberRepository).findById(member.getId());
        verify(solutionRepository).getSolutionTimes(solvedPuzzle);
        verify(solutionRepository).delete(solutionToDelete);
        verify(memberRepository).save(member);
        verify(puzzleRepository, times(2)).save(solvedPuzzle);

        verifyNoMoreInteractions(solutionRepository);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(puzzleRepository);
    }

    @Test
    public void testDeleteSolutionWhenLevelIsChangedToDifficult() {
        double initialRating = 4;
        double expectedRating = 5;
        Level initialLevel = Level.MEDIUM;
        Level expectedLevel = Level.DIFFICULT;
        Puzzle solvedPuzzle = Puzzle.builder().id(1L).rating(initialRating).level(initialLevel).build();

        int initialScore = 100;
        int expectedScore = 80;
        Member member = Member.builder().id(1L).email("email@email.hu").score(initialScore).build();

        Long solutionId = 1L;
        Solution solutionToDelete = Solution.builder().id(solutionId).member(member).puzzle(solvedPuzzle).build();
        List<Integer> solutionTimes = Arrays.asList(500, 280, 320);

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(solutionToDelete));
        when(puzzleRepository.findById(solutionToDelete.getPuzzle().getId())).thenReturn(Optional.of(solvedPuzzle));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(solutionRepository.getRatingAverage(solvedPuzzle)).thenReturn(expectedRating);
        when(solutionRepository.getSolutionTimes(solvedPuzzle)).thenReturn(solutionTimes);

        this.solutionService.deleteSolution(solutionId);

        assertEquals(expectedRating, solvedPuzzle.getRating());
        assertEquals(expectedScore, (int) member.getScore());
        assertEquals(expectedLevel, solvedPuzzle.getLevel());

        verify(solutionRepository).findById(solutionId);
        verify(puzzleRepository).findById(solutionToDelete.getPuzzle().getId());
        verify(solutionRepository).getRatingAverage(solvedPuzzle);
        verify(memberRepository).findById(member.getId());
        verify(solutionRepository).getSolutionTimes(solvedPuzzle);
        verify(solutionRepository).delete(solutionToDelete);
        verify(memberRepository).save(member);
        verify(puzzleRepository, times(2)).save(solvedPuzzle);

        verifyNoMoreInteractions(solutionRepository);
        verifyNoMoreInteractions(memberRepository);
        verifyNoMoreInteractions(puzzleRepository);
    }
}
