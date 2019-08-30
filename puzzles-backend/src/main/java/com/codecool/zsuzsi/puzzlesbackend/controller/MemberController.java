package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/leaderboard")
    public List<Member> getLeaderBoard() {
        return memberService.getLeaderBoard();
    }

    @GetMapping("/profile")
    public Member getMyProfile(@RequestHeader("Authorization") String token) {
        return memberService.getMemberFromToken(token);
    }
}
