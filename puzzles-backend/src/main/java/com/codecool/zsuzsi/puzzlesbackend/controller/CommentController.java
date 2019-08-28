package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.service.CommentService;
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

    @GetMapping("/{puzzle-id}")
    public List<Comment> getAllComments(@PathVariable("puzzle-id") Long id ) {
        return commentService.getAllCommentsOfPuzzle(id);
    }

    @PostMapping("/member")
    public List<Comment> getCommentsOfMember(@RequestBody Member member) {
        return commentService.getAllCommentsOfMember(member);
    }

    @PostMapping("/add")
    public Comment addNewComment(@RequestBody Comment comment) {
        return commentService.addNewComment(comment);
    }
}
