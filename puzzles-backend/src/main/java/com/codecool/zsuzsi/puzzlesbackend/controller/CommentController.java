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

    @GetMapping("/logged-in-member")
    public List<Comment> getLatestCommentsByLoggedInMember() {
        Member member = memberService.getLoggedInMember();
        return commentService.getLatestCommentsByMember(member);
    }

    @GetMapping("/member/{id}")
    public List<Comment> getAllCommentsByMember(@PathVariable("id") Long id) {
        return commentService.getAllCommentsByMember(id);
    }

    @PostMapping("/add")
    public Comment addNewComment(@RequestBody Comment comment) {
        Member member = memberService.getLoggedInMember();
        return commentService.addNewComment(comment, member);
    }

    @PutMapping("/update/{id}")
    public Comment updateComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
    }
}
