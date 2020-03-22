package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Comment;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.CommentDto;
import com.codecool.zsuzsi.puzzlesbackend.service.CommentService;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/{puzzle-id}")
    public List<CommentDto> getAllCommentsByPuzzle(@PathVariable("puzzle-id") Long id ) {
        return commentService.getAllCommentsByPuzzle(id).stream().map(this::convertComment).collect(Collectors.toList());
    }

    @GetMapping("/logged-in-member")
    public List<CommentDto> getLatestCommentsByLoggedInMember() {
        Member member = memberService.getLoggedInMember();
        return commentService.getLatestCommentsByMember(member).stream().map(this::convertComment).collect(Collectors.toList());
    }

    @GetMapping("/member/{id}")
    public List<CommentDto> getAllCommentsByMember(@PathVariable("id") Long id) {
        return commentService.getAllCommentsByMember(id).stream().map(this::convertComment).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public CommentDto addNewComment(@RequestBody Comment comment) {
        Member member = memberService.getLoggedInMember();
        return this.convertComment(commentService.addNewComment(comment, member));
    }

    @PutMapping("/update/{id}")
    public CommentDto updateComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        return convertComment(commentService.updateComment(id, comment));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
    }

    private CommentDto convertComment(Comment comment) {
        return this.modelMapper.map(comment, CommentDto.class);
    }
}
