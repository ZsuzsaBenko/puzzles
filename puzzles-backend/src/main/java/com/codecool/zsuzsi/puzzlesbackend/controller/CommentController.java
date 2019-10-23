package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.service.CommentService;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @GetMapping("/{puzzle-id}")
    public List<Comment> getAllCommentsByPuzzle(@PathVariable("puzzle-id") Long id ) {
        return commentService.getAllCommentsByPuzzle(id);
    }

    @GetMapping("/member")
    public List<Comment> getLatestCommentsByMember() {
        Member member = memberService.getLoggedInMember();
        return commentService.getLatestCommentsByMember(member);
    }

    @PostMapping("/add")
    public Comment addNewComment(@RequestBody Comment comment) {
        Member member = memberService.getLoggedInMember();
        return commentService.addNewComment(comment, member);
    }
}
