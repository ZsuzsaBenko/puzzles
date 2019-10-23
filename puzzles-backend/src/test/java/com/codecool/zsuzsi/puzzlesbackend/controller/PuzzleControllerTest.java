package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.PuzzleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PuzzleControllerTest {
    private static final String MAIN_URL = "/puzzles";
    private static final String TOKEN = "Bearer abcd";

    @MockBean
    private PuzzleService puzzleService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Puzzle> puzzles;
    private Member member;

    @BeforeEach
    public void init() {
        puzzles = Arrays.asList(
          Puzzle.builder().id(1L).title("puzzle1").category(Category.RIDDLE).level(Level.EASY).build(),
          Puzzle.builder().id(2L).title("puzzle2").category(Category.MATH_PUZZLE).level(Level.MEDIUM).build(),
          Puzzle.builder().id(3L).title("puzzle3").category(Category.CIPHER).level(Level.DIFFICULT).build()
        );

        member = Member.builder().email("email@email.hu").build();
    }

    @Test
    @WithMockUser
    public void testGetAllPuzzles() throws Exception {
        when(puzzleService.getAllPuzzles()).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/all")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(puzzleService).getAllPuzzles();
    }

    @Test
    @WithMockUser
    public void testGetUnsolvedPuzzleFromEachCategory() throws Exception {
        when(memberService.getLoggedInMember()).thenReturn(member);
        when(puzzleService.getUnsolvedPuzzleFromEachCategory(member)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/random")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(memberService).getLoggedInMember();
        verify(puzzleService).getUnsolvedPuzzleFromEachCategory(member);
    }

    @Test
    @WithMockUser
    public void testGetAllPuzzlesByMember() throws Exception {
        when(memberService.getLoggedInMember()).thenReturn(member);
        when(puzzleService.getAllPuzzlesByMember(member)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/member")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(memberService).getLoggedInMember();
        verify(puzzleService).getAllPuzzlesByMember(member);
    }

    @Test
    @WithMockUser
    public void testGetPuzzlesByCategory() throws Exception {
        Category category = Category.RIDDLE;
        when(puzzleService.getAllPuzzlesByCategory(category)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/{category}", category)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(puzzleService).getAllPuzzlesByCategory(category);
    }

    @Test
    @WithMockUser
    public void testGetPuzzle() throws Exception {
        Long id = 1L;
        when(puzzleService.getById(id)).thenReturn(puzzles.get(0));

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/all/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles.get(0)), responseBody);
        verify(puzzleService).getById(id);
    }

    @Test
    @WithMockUser
    public void testGetSortedPuzzles() throws Exception {
        String criteria = "titleASC";
        when(puzzleService.getSortedPuzzles(criteria)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/sort/{criteria}", criteria)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(puzzleService).getSortedPuzzles(criteria);
    }

    @Test
    @WithMockUser
    public void testGetSortedPuzzlesByCategory() throws Exception {
        String criteria = "titleASC";
        Category category = Category.MATH_PUZZLE;
        when(puzzleService.getSortedPuzzles(category, criteria)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/sort/{category}/{criteria}", category, criteria)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(puzzleService).getSortedPuzzles(category, criteria);
    }

    @Test
    @WithMockUser
    public void testAddNewPuzzle() throws Exception {
        Puzzle puzzle = puzzles.get(0);
        String requestBody = objectMapper.writeValueAsString(puzzle);

        when(memberService.getLoggedInMember()).thenReturn(member);
        when(puzzleService.addNewPuzzle(puzzle, member)).thenReturn(puzzle);

        MvcResult mvcResult = mockMvc
                .perform(
                        post(MAIN_URL + "/add")
                                .content(requestBody)
                                .header("Authorization", TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzle), responseBody);
        verify(memberService).getLoggedInMember();
        verify(puzzleService).addNewPuzzle(puzzle, member);
    }
}
