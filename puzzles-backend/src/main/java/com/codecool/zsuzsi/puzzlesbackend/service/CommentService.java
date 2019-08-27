package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.repository.CommentRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PuzzleRepository puzzleRepository;
    private final MemberRepository memberRepository;

    public List<Comment> getAllComments(Long id) {
        Puzzle puzzle = puzzleRepository.findById(id).orElse(null);
        return commentRepository.findAllByPuzzle(puzzle);
    }

    public Comment addNewComment(Comment comment) {
        Member member = memberRepository.findByEmail(comment.getMember().getEmail()).orElse(null);
        Puzzle puzzle = puzzleRepository.findById(comment.getPuzzle().getId()).orElse(null);
        comment.setMember(member);
        comment.setPuzzle(puzzle);
        return commentRepository.save(comment);
    }
}
