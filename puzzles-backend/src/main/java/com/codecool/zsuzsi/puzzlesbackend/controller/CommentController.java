package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
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

    @PostMapping("/puzzle")
    public List<Comment> getAllComments(@RequestBody Puzzle puzzle) {
        return commentService.getAllComments(puzzle);
    }

    @PostMapping("/add")
    public Comment addNewComment(@RequestBody Comment comment) {
        return commentService.addNewComment(comment);
    }
}
