package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.service.CommentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {
    private static final String MAIN_URL = "/comments";
    private static final String TOKEN = "Bearer abcd";

    @MockBean
    private CommentService commentService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Comment> comments;
    private Member member;

    @BeforeEach
    public void init() {
        comments = Arrays.asList(
          Comment.builder().id(1L).message("comment1").build(),
          Comment.builder().id(2L).message("comment2").build(),
          Comment.builder().id(3L).message("comment3").build()
        );
        member = Member.builder().email("email@email.hu").build();
    }

    @Test
    @WithMockUser
    public void testGetAllCommentsByPuzzle() throws Exception {
        Long id = 1L;
        when(commentService.getAllCommentsByPuzzle(id)).thenReturn(comments);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/{puzzle-id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(comments), responseBody);
        verify(commentService).getAllCommentsByPuzzle(id);
    }

    @Test
    @WithMockUser
    public void testGetLatestCommentsByMember() throws Exception {
        when(memberService.getMemberFromToken(TOKEN)).thenReturn(member);
        when(commentService.getLatestCommentsByMember(member)).thenReturn(comments);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/member")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(comments), responseBody);
        verify(commentService).getLatestCommentsByMember(member);
    }

    @Test
    @WithMockUser
    public void testAddNewComment() throws Exception {
        Comment comment = comments.get(0);
        when(memberService.getMemberFromToken(TOKEN)).thenReturn(member);
        when(commentService.addNewComment(comment, member)).thenReturn(comment);
        String requestBody = objectMapper.writeValueAsString(comment);

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

        assertEquals(objectMapper.writeValueAsString(comment), responseBody);
        verify(commentService).addNewComment(comment, member);
    }

}
