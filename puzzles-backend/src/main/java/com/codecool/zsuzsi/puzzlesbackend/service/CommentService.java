package com.codecool.zsuzsi.puzzlesbackend.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PuzzleRepository puzzleRepository;

    public List<Comment> getAllCommentsByPuzzle(Long id) {
        Puzzle puzzle = puzzleRepository.findById(id).orElse(null);

        if (puzzle != null) {
            log.info("Comments belonging to puzzle " + puzzle.getId() + " requested");
            return commentRepository.findAllByPuzzleOrderBySubmissionTimeAsc(puzzle);
        }
        return null;
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
        Puzzle puzzle = puzzleRepository.findById(comment.getPuzzle().getId()).orElse(null);

        if (puzzle != null) {
            comment.setMember(member);
            comment.setPuzzle(puzzle);

            log.info("New comment for puzzle " + puzzle.getId() + " created by " + member.getEmail());

            commentRepository.save(comment);
            return comment;
        }
        return null;
    }
}
