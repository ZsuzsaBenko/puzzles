package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.PuzzleNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.*;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(basePackageClasses = {PuzzleService.class})
@Import(JwtTokenServices.class)
public class PuzzleServiceTest {

    @MockBean
    private CipherMaker cipherMaker;

    @MockBean
    private PuzzleRepository puzzleRepository;

    @MockBean
    private SolutionRepository solutionRepository;

    @MockBean
    private MemberRepository memberRepositrory;

    @Autowired
    private PuzzleService puzzleService;

    private Puzzle puzzle1;
    private Puzzle puzzle2;
    private Puzzle puzzle3;
    private List<Puzzle> puzzles;

    @BeforeEach
    public void init() {
        puzzle1 = Puzzle.builder().title("Puzzle1").category(Category.RIDDLE).level(Level.EASY)
                .member(Member.builder().email("email@email.hu").build())
                .submissionTime(LocalDateTime.now()).build();
        puzzle2 = Puzzle.builder().title("Puzzle2").category(Category.MATH_PUZZLE).level(Level.MEDIUM)
                .member(Member.builder().email("email@email.hu").build())
                .submissionTime(LocalDateTime.of(2019, 9, 8, 10, 10)).build();
        puzzle3 = Puzzle.builder().title("Puzzle3").category(Category.CIPHER).level(Level.DIFFICULT)
                .member(Member.builder().email("email@email.hu").build())
                .submissionTime(LocalDateTime.of(2019, 9, 7, 10, 10)).build();
        puzzles = Arrays.asList(puzzle1, puzzle2, puzzle3);

    }

    @Test
    public void testGetById() {
        Long id = 1L;
        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle1));

        Puzzle result = puzzleService.getById(id);

        assertEquals(puzzle1, result);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testPuzzleDoesNotExist() {
        Long id = 1L;
        when(puzzleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PuzzleNotFoundException.class, () -> puzzleService.getById(id));
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testGetAllPuzzles() {
        when(puzzleRepository.findAllByOrderBySubmissionTimeDesc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getAllPuzzles();

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderBySubmissionTimeDesc();
    }

    @Test
    public void testGetAllPuzzlesByCategory() {
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategory(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getAllPuzzlesByCategory(category);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategory(category);
    }

    @Test
    public void testGetAllPuzzlesByMember() {
        Member member = Member.builder().email("email@email.hu").build();
        when(puzzleRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getAllPuzzlesByMember(member);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testGetAllPuzzlesByMemberAsAdmin() {
        Long id = 1L;
        Member member = Member.builder().email("email@email.hu").build();

        when(memberRepositrory.findById(id)).thenReturn(Optional.of(member));
        when(puzzleRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getAllPuzzlesByMember(id);

        assertIterableEquals(puzzles, result);
        verify(memberRepositrory).findById(id);
        verify(puzzleRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testGetAllPuzzlesByMemberAsAdminWithNonexistentMember() {
        Long id = 1L;
        when(memberRepositrory.findById(id)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> puzzleService.getAllPuzzlesByMember(id));
        verify(memberRepositrory).findById(id);
    }

    @Test
    public void testGetSortedPuzzlesByTitleASC() {
        String criteria = "titleASC";
        when(puzzleRepository.findAllByOrderByTitleAsc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByTitleAsc();
    }

    @Test
    public void testGetSortedPuzzlesByTitleDESC() {
        String criteria = "titleDESC";
        when(puzzleRepository.findAllByOrderByTitleDesc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByTitleDesc();
    }

    @Test
    public void testGetSortedPuzzlesByLevelASC() {
        String criteria = "levelASC";
        when(puzzleRepository.findAllByOrderByLevelAsc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByLevelAsc();
    }

    @Test
    public void testGetSortedPuzzlesByLevelDESC() {
        String criteria = "levelDESC";
        when(puzzleRepository.findAllByOrderByLevelDesc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByLevelDesc();
    }

    @Test
    public void testGetSortedPuzzlesByRatingASC() {
        String criteria = "ratingASC";
        when(puzzleRepository.findAllByOrderByRatingAsc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByRatingAsc();
    }

    @Test
    public void testGetSortedPuzzlesByRatingDESC() {
        String criteria = "ratingDESC";
        when(puzzleRepository.findAllByOrderByRatingDesc()).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByOrderByRatingDesc();
    }

    @Test
    public void testGetSortedPuzzlesByTitleASCWithCategory() {
        String criteria = "titleASC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByTitleAsc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByTitleAsc(category);
    }

    @Test
    public void testGetSortedPuzzlesByTitleDESCWithCategory() {
        String criteria = "titleDESC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByTitleDesc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByTitleDesc(category);
    }

    @Test
    public void testGetSortedPuzzlesByLevelASCWithCategory() {
        String criteria = "levelASC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByLevelAsc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByLevelAsc(category);
    }

    @Test
    public void testGetSortedPuzzlesByLevelDESCWithCategory() {
        String criteria = "levelDESC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByLevelDesc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByLevelDesc(category);
    }

    @Test
    public void testGetSortedPuzzlesByRatingASCWithCategory() {
        String criteria = "ratingASC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByRatingAsc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByRatingAsc(category);
    }

    @Test
    public void testGetSortedPuzzlesByRatingDESCWithCategory() {
        String criteria = "ratingDESC";
        Category category = Category.RIDDLE;
        when(puzzleRepository.findAllByCategoryOrderByRatingDesc(category)).thenReturn(puzzles);

        List<Puzzle> result = puzzleService.getSortedPuzzles(category, criteria);

        assertIterableEquals(puzzles, result);
        verify(puzzleRepository).findAllByCategoryOrderByRatingDesc(category);
    }

    @Test
    public void testGetUnsolvedPuzzlesFromEachCategoryWhenUserHasSolvedPuzzles() {
        Member member = Member.builder().email("email@email.hu").build();

        when(solutionRepository.findAllByMember(member)).thenReturn(Arrays.asList(
                Solution.builder().puzzle(puzzle1).build(),
                Solution.builder().puzzle(puzzle2).build(),
                Solution.builder().puzzle(puzzle3).build()
        ));

        Puzzle puzzleOne = Puzzle.builder().title("1").category(Category.RIDDLE).build();
        Puzzle puzzleTwo = Puzzle.builder().title("2").category(Category.RIDDLE).build();
        Puzzle puzzleThree = Puzzle.builder().title("3").category(Category.MATH_PUZZLE).build();
        Puzzle puzzleFour = Puzzle.builder().title("4").category(Category.MATH_PUZZLE).build();
        Puzzle puzzleFive = Puzzle.builder().title("5").category(Category.PICTURE_PUZZLE).build();
        Puzzle puzzleSix = Puzzle.builder().title("6").category(Category.PICTURE_PUZZLE).build();
        Puzzle puzzleSeven = Puzzle.builder().title("7").category(Category.WORD_PUZZLE).build();
        Puzzle puzzleEight = Puzzle.builder().title("8").category(Category.WORD_PUZZLE).build();
        Puzzle puzzleNine = Puzzle.builder().title("9").category(Category.CIPHER).build();
        Puzzle puzzleTen = Puzzle.builder().title("10").category(Category.CIPHER).build();

        when(puzzleRepository.findUnsolved(puzzles, Category.RIDDLE))
                .thenReturn(Arrays.asList(puzzleOne, puzzleTwo));
        when(puzzleRepository.findUnsolved(puzzles, Category.MATH_PUZZLE))
                .thenReturn(Arrays.asList(puzzleThree, puzzleFour));
        when(puzzleRepository.findUnsolved(puzzles, Category.PICTURE_PUZZLE))
                .thenReturn(Arrays.asList(puzzleFive, puzzleSix));
        when(puzzleRepository.findUnsolved(puzzles, Category.WORD_PUZZLE))
                .thenReturn(Arrays.asList(puzzleSeven, puzzleEight));
        when(puzzleRepository.findUnsolved(puzzles, Category.CIPHER))
                .thenReturn(Arrays.asList(puzzleNine, puzzleTen));

        List<Puzzle> result = puzzleService.getUnsolvedPuzzleFromEachCategory(member);

        assertIterableEquals(Arrays.asList(puzzleOne, puzzleThree, puzzleFive, puzzleSeven, puzzleNine), result);

        verify(solutionRepository).findAllByMember(member);
        verify(puzzleRepository).findUnsolved(puzzles, Category.RIDDLE);
        verify(puzzleRepository).findUnsolved(puzzles, Category.MATH_PUZZLE);
        verify(puzzleRepository).findUnsolved(puzzles, Category.PICTURE_PUZZLE);
        verify(puzzleRepository).findUnsolved(puzzles, Category.WORD_PUZZLE);
        verify(puzzleRepository).findUnsolved(puzzles, Category.CIPHER);
    }

    @Test
    public void testGetUnsolvedPuzzlesFromEachCategoryWhenUserHasNoSolvedPuzzles() {
        Member member = Member.builder().email("email@email.hu").build();

        when(solutionRepository.findAllByMember(member)).thenReturn(new ArrayList<>());

        Puzzle one = Puzzle.builder().title("1").category(Category.RIDDLE).build();
        Puzzle two = Puzzle.builder().title("3").category(Category.MATH_PUZZLE).build();
        Puzzle three = Puzzle.builder().title("5").category(Category.PICTURE_PUZZLE).build();
        Puzzle four = Puzzle.builder().title("7").category(Category.WORD_PUZZLE).build();
        Puzzle five = Puzzle.builder().title("9").category(Category.CIPHER).build();

        when(puzzleRepository.findFirstByCategory(Category.RIDDLE)).thenReturn(Optional.of(one));
        when(puzzleRepository.findFirstByCategory(Category.MATH_PUZZLE)).thenReturn(Optional.of(two));
        when(puzzleRepository.findFirstByCategory(Category.PICTURE_PUZZLE)).thenReturn(Optional.of(three));
        when(puzzleRepository.findFirstByCategory(Category.WORD_PUZZLE)).thenReturn(Optional.of(four));
        when(puzzleRepository.findFirstByCategory(Category.CIPHER)).thenReturn(Optional.of(five));

        List<Puzzle> result = puzzleService.getUnsolvedPuzzleFromEachCategory(member);

        assertIterableEquals(Arrays.asList(one, two, three, four, five), result);

        verify(solutionRepository).findAllByMember(member);
        verify(puzzleRepository).findFirstByCategory(Category.RIDDLE);
        verify(puzzleRepository).findFirstByCategory(Category.MATH_PUZZLE);
        verify(puzzleRepository).findFirstByCategory(Category.PICTURE_PUZZLE);
        verify(puzzleRepository).findFirstByCategory(Category.WORD_PUZZLE);
        verify(puzzleRepository).findFirstByCategory(Category.CIPHER);
    }

    @Test
    public void testAddNewPuzzleWhenItIsNotCipher() {
        Member member = Member.builder().email("test@test.hu").build();
        Puzzle newPuzzle = Puzzle.builder().title("Puzzle1").category(Category.RIDDLE).level(Level.EASY).answer("answer")
                .member(Member.builder().email("email@email.hu").build()).build();
        Puzzle expected = Puzzle.builder().title("Puzzle1").category(Category.RIDDLE).level(Level.EASY).answer("answer")
                .member(Member.builder().email("test@test.hu").build()).build();

        when(puzzleRepository.save(newPuzzle)).thenReturn(expected);

        Puzzle result = puzzleService.addNewPuzzle(newPuzzle, member);

        assertEquals(expected, result);
        verify(puzzleRepository).save(newPuzzle);
    }

    @Test
    public void testAddNewPuzzleWhenItIsEasyCipher() {
        Member member = Member.builder().email("test@test.hu").build();
        Puzzle newPuzzle = Puzzle.builder().title("Puzzle1").category(Category.CIPHER).level(Level.EASY).answer("answer")
                .member(Member.builder().email("email@email.hu").build()).build();
        String instruction = "Fejtsd meg a titkosírást! Az abc minden betűjét \"arréb toltuk\" valamennyivel.";
        Puzzle expected = Puzzle.builder().title("Puzzle1").category(Category.CIPHER).level(Level.EASY).answer("answer")
                .instruction(instruction)
                .puzzleItem("epűáiü").member(Member.builder().email("test@test.hu").build()).build();

        when(cipherMaker.createShiftCipher(eq(newPuzzle.getAnswer()), anyInt())).thenReturn(expected.getPuzzleItem());
        when(puzzleRepository.save(newPuzzle)).thenReturn(expected);

        Puzzle result = puzzleService.addNewPuzzle(newPuzzle, member);

        assertEquals(expected, result);
        verify(cipherMaker).createShiftCipher(eq(newPuzzle.getAnswer()), anyInt());
        verify(puzzleRepository).save(newPuzzle);
    }

    @Test
    public void testAddNewPuzzleWhenItIsDifficultCipher() {
        Member member = Member.builder().email("test@test.hu").build();
        Puzzle newPuzzle = Puzzle.builder().title("Puzzle1").category(Category.CIPHER).level(Level.DIFFICULT)
                .answer("answer").member(Member.builder().email("email@email.hu").build()).build();
        Map<String, Map<String, String>> cipher = new HashMap<>();
        int helperLetterNumber = 3;

        String instruction = "Fejtsd meg a titkosírást! Az abc minden betűje egy másik betűnek felel meg, " +
                "teljesen véletlenszerűen. Egy kis segítség: {}. Az egyenlőségjel bal oldalán az eredeti betű áll, " +
                "a jobb oldalon a titkosírásban használt megfelelője.";
        Puzzle expected = Puzzle.builder().title("Puzzle1").category(Category.CIPHER).level(Level.DIFFICULT)
                .instruction(instruction)
                .puzzleItem("").answer("answer").member(Member.builder().email("test@test.hu").build()).build();

        when(cipherMaker.createRandomCipher(newPuzzle.getAnswer(), helperLetterNumber)).thenReturn(cipher);
        when(puzzleRepository.save(newPuzzle)).thenReturn(expected);

        Puzzle result = puzzleService.addNewPuzzle(newPuzzle, member);

        assertEquals(expected, result);
        verify(cipherMaker).createRandomCipher(newPuzzle.getAnswer(), helperLetterNumber);
        verify(puzzleRepository).save(newPuzzle);
    }

    @Test
    public void testUpdateNonexistentPuzzle() {
        Long id = 1L;
        when(puzzleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PuzzleNotFoundException.class, () -> puzzleService.updatePuzzle(id, puzzle1));
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testUpdateNormalPuzzle() {
        Long id = 1L;
        Puzzle updatePuzzle = Puzzle.builder().title("Updated Puzzle").category(Category.RIDDLE)
                .instruction("instruction").puzzleItem("puzzle item").answer("answer").build();
        Puzzle puzzleToUpdate = Puzzle.builder().title("Puzzle").category(Category.RIDDLE)
                .instruction("abc").puzzleItem("abc").answer("abc").build();

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzleToUpdate));

        puzzleService.updatePuzzle(id, updatePuzzle);

        assertEquals(updatePuzzle, puzzleToUpdate);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testUpdatePicturePuzzle() {
        Long id = 1L;
        Puzzle updatePuzzle = Puzzle.builder().title("Updated Puzzle").category(Category.PICTURE_PUZZLE)
                .instruction("instruction").puzzleItem("puzzle item").answer("answer").build();
        Puzzle puzzleToUpdate = Puzzle.builder().title("Puzzle").category(Category.PICTURE_PUZZLE)
                .instruction("abc").puzzleItem("abc").answer("abc").build();
        Puzzle expectedPuzzle = Puzzle.builder().title("Updated Puzzle").category(Category.PICTURE_PUZZLE)
                .instruction("instruction").puzzleItem("abc").answer("answer").build();

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzleToUpdate));

        puzzleService.updatePuzzle(id, updatePuzzle);

        assertEquals(expectedPuzzle, puzzleToUpdate);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testUpdateCipherPuzzle() {
        Long id = 1L;
        Puzzle updatePuzzle = Puzzle.builder().title("Updated Puzzle").category(Category.CIPHER)
                .instruction("instruction").puzzleItem("puzzle item").answer("answer").build();
        Puzzle puzzleToUpdate = Puzzle.builder().title("Puzzle").category(Category.CIPHER)
                .instruction("abc").puzzleItem("abc").answer("abc").build();
        Puzzle expectedPuzzle = Puzzle.builder().title("Updated Puzzle").category(Category.CIPHER)
                .instruction("abc").puzzleItem("abc").answer("abc").build();

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzleToUpdate));

        puzzleService.updatePuzzle(id, updatePuzzle);

        assertEquals(expectedPuzzle, puzzleToUpdate);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testDeleteNonexistentPuzzle() {
        Long id = 1L;
        when(puzzleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PuzzleNotFoundException.class, () -> puzzleService.deletePuzzle(id));
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testDeleteEasyPuzzle() {
        Long id = 1L;
        List<Member> membersWhoSolvedPuzzle = Arrays.asList(
                Member.builder().id(1L).score(30).build(),
                Member.builder().id(2L).score(20).build(),
                Member.builder().id(3L).score(10).build()
        );

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle1));
        when(solutionRepository.getMembersWhoSolvedPuzzle(puzzle1)).thenReturn(membersWhoSolvedPuzzle);
        doNothing().when(puzzleRepository).delete(puzzle1);

        puzzleService.deletePuzzle(id);

        assertEquals(Integer.valueOf(20), membersWhoSolvedPuzzle.get(0).getScore());
        assertEquals(Integer.valueOf(10), membersWhoSolvedPuzzle.get(1).getScore());
        assertEquals(Integer.valueOf(0), membersWhoSolvedPuzzle.get(2).getScore());

        verify(puzzleRepository).findById(id);
        verify(solutionRepository).getMembersWhoSolvedPuzzle(puzzle1);
        verify(puzzleRepository).delete(puzzle1);
    }

    @Test
    public void testDeleteMediumPuzzle() {
        Long id = 1L;
        List<Member> membersWhoSolvedPuzzle = Arrays.asList(
                Member.builder().id(1L).score(50).build(),
                Member.builder().id(2L).score(40).build(),
                Member.builder().id(3L).score(30).build()
        );

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle2));
        when(solutionRepository.getMembersWhoSolvedPuzzle(puzzle2)).thenReturn(membersWhoSolvedPuzzle);
        doNothing().when(puzzleRepository).delete(puzzle2);

        puzzleService.deletePuzzle(id);

        assertEquals(Integer.valueOf(30), membersWhoSolvedPuzzle.get(0).getScore());
        assertEquals(Integer.valueOf(20), membersWhoSolvedPuzzle.get(1).getScore());
        assertEquals(Integer.valueOf(10), membersWhoSolvedPuzzle.get(2).getScore());

        verify(puzzleRepository).findById(id);
        verify(solutionRepository).getMembersWhoSolvedPuzzle(puzzle2);
        verify(puzzleRepository).delete(puzzle2);
    }

    @Test
    public void testDeleteDifficultPuzzle() {
        Long id = 1L;
        List<Member> membersWhoSolvedPuzzle = Arrays.asList(
                Member.builder().id(1L).score(65).build(),
                Member.builder().id(2L).score(60).build(),
                Member.builder().id(3L).score(50).build()
        );

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle3));
        when(solutionRepository.getMembersWhoSolvedPuzzle(puzzle3)).thenReturn(membersWhoSolvedPuzzle);
        doNothing().when(puzzleRepository).delete(puzzle3);

        puzzleService.deletePuzzle(id);

        assertEquals(Integer.valueOf(15), membersWhoSolvedPuzzle.get(0).getScore());
        assertEquals(Integer.valueOf(10), membersWhoSolvedPuzzle.get(1).getScore());
        assertEquals(Integer.valueOf(0), membersWhoSolvedPuzzle.get(2).getScore());

        verify(puzzleRepository).findById(id);
        verify(solutionRepository).getMembersWhoSolvedPuzzle(puzzle3);
        verify(puzzleRepository).delete(puzzle3);
    }

    @Test
    public void testCheckAnswerWithCorrectAnswer() {
        Long id = 1L;
        String answer = "good Solution ";
        Puzzle puzzle = Puzzle.builder().answer("good solution").build();

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle));

        Boolean result = puzzleService.checkAnswer(id, answer);

        assertTrue(result);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testCheckAnswerWithInCorrectAnswer() {
        Long id = 1L;
        String answer = "bad Solution ";
        Puzzle puzzle = Puzzle.builder().answer("good solution").build();

        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle));

        Boolean result = puzzleService.checkAnswer(id, answer);

        assertFalse(result);
        verify(puzzleRepository).findById(id);
    }
}

