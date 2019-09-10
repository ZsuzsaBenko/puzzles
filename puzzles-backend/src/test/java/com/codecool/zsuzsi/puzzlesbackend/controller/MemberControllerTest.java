package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
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
class MemberControllerTest {

    private static final String MAIN_URL = "/members";

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Member member;
    private String token = "Bearer abcdef";

    @BeforeEach
    public void init() {
        member = Member.builder().username("User").email("email@email.hu").build();
    }

    @Test
    @WithMockUser
    public void testGetMyProfile() throws Exception {
        when(memberService.getMemberFromToken(token)).thenReturn(member);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get(MAIN_URL + "/profile")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(member), responseBody);
        verify(memberService).getMemberFromToken(token);
    }

    @Test
    @WithMockUser
    public void testGetTopLeaderboard() throws Exception {
        List<Member> leaderboard = Arrays.asList(
                Member.builder().id(1L).email("email@email.hu").build(),
                Member.builder().id(2L).email("email@email.hu").build(),
                Member.builder().id(3L).email("email@email.hu").build()
        );
        when(memberService.getTopLeaderBoard()).thenReturn(leaderboard);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get(MAIN_URL + "/top-leaderboard")
                                .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(leaderboard), responseBody);
        verify(memberService).getTopLeaderBoard();
    }

    @Test
    @WithMockUser
    public void testGetFullLeaderboard() throws Exception {
        List<Member> leaderboard = Arrays.asList(
                Member.builder().id(1L).email("email@email.hu").build(),
                Member.builder().id(2L).email("email@email.hu").build(),
                Member.builder().id(3L).email("email@email.hu").build()
        );
        when(memberService.getFullLeaderBoard()).thenReturn(leaderboard);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get(MAIN_URL + "/full-leaderboard")
                                .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(leaderboard), responseBody);
        verify(memberService).getFullLeaderBoard();
    }

    @Test
    @WithMockUser
    public void testUpdateProfile() throws Exception{
        UserCredentials data = UserCredentials.builder().email("email@email.hu").password("password").build();
        String requestBody = this.objectMapper.writeValueAsString(data);

        when(memberService.updateProfile(token, data)).thenReturn(member);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        put(MAIN_URL + "/profile/update")
                                .content(requestBody)
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(member), responseBody);
        verify(memberService).updateProfile(token, data);
    }
}
