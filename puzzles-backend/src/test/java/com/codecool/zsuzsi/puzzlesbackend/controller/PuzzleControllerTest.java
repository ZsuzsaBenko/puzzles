package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.PuzzleDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private List<PuzzleDto> puzzleDtos;

    private Member member;

    @BeforeEach
    public void init() {
        puzzles = Arrays.asList(
                Puzzle.builder().id(1L).title("puzzle1").category(Category.RIDDLE).level(Level.EASY).answer("Answer1").build(),
                Puzzle.builder().id(2L).title("puzzle2").category(Category.MATH_PUZZLE).level(Level.MEDIUM).answer("Answer2").build(),
                Puzzle.builder().id(3L).title("puzzle3").category(Category.CIPHER).level(Level.DIFFICULT).answer("Answer3").build()
        );
        puzzleDtos = Arrays.asList(
                PuzzleDto.builder().id(1L).title("puzzle1").category(Category.RIDDLE).level(Level.EASY).build(),
                PuzzleDto.builder().id(2L).title("puzzle2").category(Category.MATH_PUZZLE).level(Level.MEDIUM).build(),
                PuzzleDto.builder().id(3L).title("puzzle3").category(Category.CIPHER).level(Level.DIFFICULT).build()
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
        verify(memberService).getLoggedInMember();
        verify(puzzleService).getUnsolvedPuzzleFromEachCategory(member);
    }

    @Test
    @WithMockUser
    public void testGetAllPuzzlesByLoggedInMember() throws Exception {
        when(memberService.getLoggedInMember()).thenReturn(member);
        when(puzzleService.getAllPuzzlesByMember(member)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/logged-in-member")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
        verify(memberService).getLoggedInMember();
        verify(puzzleService).getAllPuzzlesByMember(member);
    }

    @Test
    @WithMockUser
    public void testGetAllPuzzlesByAnyMemberWithNormalUser() throws Exception {
        Long id = 1L;
        mockMvc
                .perform(
                        get(MAIN_URL + "/member/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllPuzzlesByAnyMemberWithAdminUser() throws Exception {
        Long id = 1L;
        when(puzzleService.getAllPuzzlesByMember(id)).thenReturn(puzzles);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/member/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(puzzles), responseBody);
        verify(puzzleService).getAllPuzzlesByMember(id);
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos.get(0)), responseBody);
        verify(puzzleService).getById(id);
    }

    @Test
    @WithMockUser
    public void testGetPuzzleForAdminWithNormalUser() throws Exception {
        Long id = 1L;
        mockMvc
                .perform(
                        get(MAIN_URL + "/all/{id}/admin", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetPuzzleForAdminWithAdminUser() throws Exception {
        Long id = 1L;
        when(puzzleService.getById(id)).thenReturn(puzzles.get(0));

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/all/{id}/admin", id)
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
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

        assertEquals(objectMapper.writeValueAsString(puzzleDtos), responseBody);
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

    @Test
    @WithMockUser
    public void testUpdatePuzzleWithNormalUser() throws Exception {
        Long id = 1L;
        String requestBody = objectMapper.writeValueAsString(puzzles.get(0));

        mockMvc.perform(
                put(MAIN_URL + "/update/{id}", id)
                        .content(requestBody)
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdatePuzzleWithAdminUser() throws Exception {
        Long id = 1L;
        Puzzle updatePuzzle = puzzles.get(0);
        String requestBody = objectMapper.writeValueAsString(updatePuzzle);
        when(puzzleService.updatePuzzle(id, updatePuzzle)).thenReturn(updatePuzzle);

        MvcResult mvcResult = mockMvc
                .perform(
                        put(MAIN_URL + "/update/{id}", id)
                                .content(requestBody)
                                .header("Authorization", TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(updatePuzzle), responseBody);
        verify(puzzleService).updatePuzzle(id, updatePuzzle);
    }

    @Test
    @WithMockUser
    public void testDeletePuzzleWithNormalUser() throws Exception {
        Long id = 1L;
        mockMvc.perform(
                delete(MAIN_URL + "/delete/{id}", id)
                        .header("Authorization", TOKEN)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeletePuzzleWithAdminUser() throws Exception {
        Long id = 1L;
        doNothing().when(puzzleService).deletePuzzle(id);

        MvcResult mvcResult = mockMvc
                .perform(
                        delete(MAIN_URL + "/delete/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        assertTrue(responseBody.isEmpty());
        verify(puzzleService).deletePuzzle(id);

    }
}
