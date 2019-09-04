package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
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

    @GetMapping("/top-leaderboard")
    public List<Member> getTopLeaderBoard() {
        return memberService.getTopLeaderBoard();
    }

    @GetMapping("/full-leaderboard")
    public List<Member> getFullLeaderBoard() {
        return memberService.getFullLeaderBoard();
    }

    @GetMapping("/profile")
    public Member getMyProfile(@RequestHeader("Authorization") String token) {
        return memberService.getMemberFromToken(token);
    }

    @PutMapping("/profile/update")
    public Member updateProfile(@RequestHeader("Authorization") String token,
                                @RequestBody UserCredentials data) {
        log.info(data.toString());
        return memberService.updateProfile(token, data);
    }
}
