package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.MemberDto;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.SolutionDto;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.SolutionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SolutionControllerTest {

    private static final String MAIN_URL = "/solutions";
    private static final String TOKEN = "Bearer abcd";

    @MockBean
    private SolutionService solutionService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    private MemberDto memberDto;

    @BeforeEach
    public void init() {
        member = Member.builder().email("email@email.hu").username("Username").build();
        memberDto = MemberDto.builder().username("Username").build();
    }

    @Test
    @WithMockUser
    public void testGetAllSolutionsByLoggedInMember() throws Exception {
        List<Solution> solutions = Arrays.asList(
                Solution.builder().id(1L).member(member).seconds(10).rating(3).build(),
                Solution.builder().id(2L).member(member).seconds(20).rating(4).build(),
                Solution.builder().id(3L).member(member).seconds(30).rating(5).build()
        );

        List<SolutionDto> solutionDtos = Arrays.asList(
                SolutionDto.builder().id(1L).member(memberDto).seconds(10).rating(3).build(),
                SolutionDto.builder().id(2L).member(memberDto).seconds(20).rating(4).build(),
                SolutionDto.builder().id(3L).member(memberDto).seconds(30).rating(5).build()
        );

        when(memberService.getLoggedInMember()).thenReturn(member);
        when(solutionService.getAllSolutionsByMember(member)).thenReturn(solutions);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/logged-in-member")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(solutionDtos), responseBody);
        verify(memberService).getLoggedInMember();
        verify(solutionService).getAllSolutionsByMember(member);
    }

    @Test
    @WithMockUser
    public void testGetAllSolutionsByMemberWithNormalUser() throws Exception {
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
    public void testGetAllSolutionsByMemberWithAdminUser() throws Exception {
        Long id = 1L;
        List<Solution> solutions = Arrays.asList(
                Solution.builder().id(1L).member(member).seconds(10).rating(3).build(),
                Solution.builder().id(2L).member(member).seconds(20).rating(4).build(),
                Solution.builder().id(3L).member(member).seconds(30).rating(5).build()
        );

        when(solutionService.getAllSolutionsByMember(id)).thenReturn(solutions);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/member/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(solutions), responseBody);
        verify(solutionService).getAllSolutionsByMember(id);
    }

    @Test
    @WithMockUser
    public void testSaveSolution() throws Exception {
        Solution solution = Solution.builder().id(1L).member(member).seconds(10).rating(3).build();
        SolutionDto solutionDto = SolutionDto.builder().id(1L).member(memberDto).seconds(10).rating(3).build();

        when(memberService.getLoggedInMember()).thenReturn(member);
        when(solutionService.saveSolution(solution, member)).thenReturn(solution);

        String requestBody = objectMapper.writeValueAsString(solution);
        MvcResult mvcResult = mockMvc
                .perform(
                        post(MAIN_URL + "/save")
                                .content(requestBody)
                                .header("Authorization", TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(solutionDto), responseBody);
        verify(memberService).getLoggedInMember();
        verify(solutionService).saveSolution(solution, member);
    }

    @Test
    @WithMockUser
    public void testDeleteSolutionWithNormalUser() throws Exception {
        Long id = 1L;

        mockMvc
                .perform(
                        delete(MAIN_URL + "delete/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteSolutionWithAdminUser() throws Exception {
        Long id = 1L;

        MvcResult mvcResult = mockMvc
                .perform(
                        delete(MAIN_URL + "/delete/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        assertTrue(responseBody.isEmpty());
        verify(solutionService).deleteSolution(id);
    }
}
