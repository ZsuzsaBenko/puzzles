package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.CommentNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.repository.CommentRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ComponentScan(basePackageClasses = {CommentService.class})
@ActiveProfiles("test")
@Import({JwtTokenServices.class, CipherMaker.class})
class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PuzzleRepository puzzleRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private CommentService commentService;


    @Test
    public void testGetAllCommentsByPuzzle() {
        Long id = 1L;
        Puzzle puzzle = Puzzle.builder().id(id).title("puzzle").build();
        List<Comment> expected = Arrays.asList(
                Comment.builder().message("first").puzzle(puzzle).build(),
                Comment.builder().message("second").puzzle(puzzle).build(),
                Comment.builder().message("third").puzzle(puzzle).build()
        );
        when(puzzleRepository.findById(id)).thenReturn(Optional.of(puzzle));
        when(commentRepository.findAllByPuzzleOrderBySubmissionTimeAsc(puzzle)).thenReturn(expected);

        List<Comment> result = commentService.getAllCommentsByPuzzle(id);

        assertIterableEquals(expected, result);
        verify(puzzleRepository).findById(id);
        verify(commentRepository).findAllByPuzzleOrderBySubmissionTimeAsc(puzzle);
    }

    @Test
    public void testLatestCommentsByMember() {
        Member member = Member.builder().email("eamil@email.hu").build();
        List<Comment> comments = Arrays.asList(
                Comment.builder().message("first").member(member).puzzle(Puzzle.builder().id(1L).build()).build(),
                Comment.builder().message("second").member(member).puzzle(Puzzle.builder().id(1L).build()).build(),
                Comment.builder().message("third").member(member).puzzle(Puzzle.builder().id(2L).build()).build()
        );

        List<Comment> expected = Arrays.asList(
                Comment.builder().message("first").member(member).puzzle(Puzzle.builder().id(1L).build()).build(),
                Comment.builder().message("third").member(member).puzzle(Puzzle.builder().id(2L).build()).build()
        );

        when(commentRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(comments);

        List<Comment> result = commentService.getLatestCommentsByMember(member);

        assertIterableEquals(expected, result);
        verify(commentRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testGetAllCommentsByMemberWithNonexistentMember() {
        Long id = 1L;
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> commentService.getAllCommentsByMember(id));
        verify(memberRepository).findById(id);
    }

    @Test
    public void testGetAllCommentsByMember() {
        Long id = 1L;
        Member member = Member.builder().id(id).build();
        List<Comment> expected = Arrays.asList(
                Comment.builder().message("first").member(member).puzzle(Puzzle.builder().id(1L).build()).build(),
                Comment.builder().message("second").member(member).puzzle(Puzzle.builder().id(1L).build()).build(),
                Comment.builder().message("third").member(member).puzzle(Puzzle.builder().id(2L).build()).build()
        );

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        when(commentRepository.findAllByMemberOrderBySubmissionTimeDesc(member)).thenReturn(expected);

        List<Comment> comments = commentService.getAllCommentsByMember(id);

        assertIterableEquals(expected, comments);
        verify(memberRepository).findById(id);
        verify(commentRepository).findAllByMemberOrderBySubmissionTimeDesc(member);
    }

    @Test
    public void testAddNewComment() {
        Long id = 1L;
        Member member = Member.builder().email("email@email.hu").build();
        Puzzle puzzle = Puzzle.builder().id(id).title("title").build();
        Comment newComment = Comment.builder().message("message").puzzle(Puzzle.builder().id(id).build()).build();
        Comment expected = Comment.builder().message("message").puzzle(puzzle).member(member).build();

        when(puzzleRepository.findById(newComment.getPuzzle().getId())).thenReturn(Optional.of(puzzle));

        Comment result = commentService.addNewComment(newComment, member);

        assertEquals(expected, result);
        verify(puzzleRepository).findById(id);
    }

    @Test
    public void testUpdateNonExistentComment() {
        Long id = 1L;
        Comment comment = Comment.builder().message("abc").build();
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(id, comment));
        verify(commentRepository).findById(id);
    }

    @Test
    public void testUpdateComment() {
        Long id = 1L;
        Comment prevComment = Comment.builder().message("abc").build();
        Comment updatedComment = Comment.builder().message("updated").build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(prevComment));

        Comment result = commentService.updateComment(id, updatedComment);

        assertEquals(updatedComment, result);
        verify(commentRepository).findById(id);
    }

    @Test
    public void testDeleteNonexistentComment() {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(id));
        verify(commentRepository).findById(id);
    }

    @Test
    public void testDeleteComment() {
        Long id = 1L;
        Comment commentToDelete = Comment.builder().message("first").build();

        when(commentRepository.findById(id)).thenReturn(Optional.of(commentToDelete));
        doNothing().when(commentRepository).delete(commentToDelete);

        commentService.deleteComment(id);

        verify(commentRepository).findById(id);
        verify(commentRepository).delete(commentToDelete);
    }
}
