package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.CommentDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private List<CommentDto> commentDtos;
    private Member member;

    @BeforeEach
    public void init() {
        member = Member.builder().email("email@email.hu").build();
        comments = Arrays.asList(
          Comment.builder().id(1L).message("comment1").member(member).build(),
          Comment.builder().id(2L).message("comment2").member(member).build(),
          Comment.builder().id(3L).message("comment3").member(member).build()
        );
        commentDtos = Arrays.asList(
                CommentDto.builder().id(1L).message("comment1").build(),
                CommentDto.builder().id(2L).message("comment2").build(),
                CommentDto.builder().id(3L).message("comment3").build()
        );
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

        assertEquals(objectMapper.writeValueAsString(commentDtos), responseBody);
        verify(commentService).getAllCommentsByPuzzle(id);
    }

    @Test
    @WithMockUser
    public void testGetLatestCommentsByLoggedInMember() throws Exception {
        when(memberService.getLoggedInMember()).thenReturn(member);
        when(commentService.getLatestCommentsByMember(member)).thenReturn(comments);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/logged-in-member")
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDtos), responseBody);
        verify(memberService).getLoggedInMember();
        verify(commentService).getLatestCommentsByMember(member);
    }

    @Test
    @WithMockUser
    public void testGetAllCommentsByMemberWithNormalUser() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                        get(MAIN_URL + "/member/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllCommentsByMemberWithAdminUser() throws Exception {
        Long id = 1L;
        when(commentService.getAllCommentsByMember(id)).thenReturn(comments);

        MvcResult mvcResult = mockMvc
                .perform(
                        get(MAIN_URL + "/member/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDtos), responseBody);
        verify(commentService).getAllCommentsByMember(id);

    }

    @Test
    @WithMockUser
    public void testAddNewComment() throws Exception {
        Comment comment = comments.get(0);
        CommentDto commentDto = commentDtos.get(0);

        when(memberService.getLoggedInMember()).thenReturn(member);
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

        assertEquals(objectMapper.writeValueAsString(commentDto), responseBody);
        verify(memberService).getLoggedInMember();
        verify(commentService).addNewComment(comment, member);
    }

    @Test
    @WithMockUser
    public void testUpdateComment() throws Exception {
        Long id = 1L;
        Comment comment = comments.get(0);
        Comment updatedComment = comments.get(1);
        CommentDto updatedCommentDto = commentDtos.get(1);

        when(commentService.updateComment(id, comment)).thenReturn(updatedComment);
        String requestBody = objectMapper.writeValueAsString(comment);

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

        assertEquals(objectMapper.writeValueAsString(updatedCommentDto), responseBody);
        verify(commentService).updateComment(id, comment);
    }

    @Test
    @WithMockUser
    public void testDeleteCommentWithNormalUser() throws Exception {
        Long id = 1L;
        mockMvc.perform(
                        delete(MAIN_URL + "/delete/{id}", id)
                                .header("Authorization", TOKEN)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteCommentWithAdminUser() throws Exception {
        Long id = 1L;
        doNothing().when(commentService).deleteComment(id);

        MvcResult mvcResult = mockMvc
                .perform(
                delete(MAIN_URL + "/delete/{id}", id)
                        .header("Authorization", TOKEN)
        )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        assertTrue(responseBody.isEmpty());
        verify(commentService).deleteComment(id);
    }

}
