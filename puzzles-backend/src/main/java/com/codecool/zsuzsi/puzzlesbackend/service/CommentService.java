package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.CommentNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.PuzzleNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.repository.CommentRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PuzzleRepository puzzleRepository;

    public List<Comment> getAllCommentsByPuzzle(Long id) {
        Optional<Puzzle> puzzle = puzzleRepository.findById(id);

        if (puzzle.isEmpty()) throw new PuzzleNotFoundException();

        log.info("Comments belonging to puzzle " + puzzle.get().getId() + " requested");
        return commentRepository.findAllByPuzzleOrderBySubmissionTimeAsc(puzzle.get());
    }

    public List<Comment> getLatestCommentsByMember(Member member) {
        log.info("Latest comments belonging to member " + member.getEmail() + " requested");
        List<Comment> commentsByMember = commentRepository.findAllByMemberOrderBySubmissionTimeDesc(member);
        List<Comment> latestComments = new ArrayList<>();
        List<Long> puzzleIDs = new ArrayList<>();

        if (commentsByMember.size() > 0) {
            for (Comment comment : commentsByMember) {
               if (!puzzleIDs.contains(comment.getPuzzle().getId())) {
                   latestComments.add(comment);
                   puzzleIDs.add(comment.getPuzzle().getId());
               }
            }
        }
        return latestComments;
    }

    public Comment addNewComment(Comment comment, Member member) {
        Optional<Puzzle> puzzle = puzzleRepository.findById(comment.getPuzzle().getId());

        if (puzzle.isEmpty()) throw new PuzzleNotFoundException();

        comment.setMember(member);
        comment.setPuzzle(puzzle.get());

        log.info("New comment for puzzle " + puzzle.get().getId() + " created by " + member.getEmail());

        commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(Long id) {
        log.info("Deletion of comment with id " + id + " requested");
        Optional<Comment> commentToBeDeleted = commentRepository.findById(id);

        if (commentToBeDeleted.isEmpty()) throw new CommentNotFoundException();

        commentRepository.delete(commentToBeDeleted.get());
    }
}
