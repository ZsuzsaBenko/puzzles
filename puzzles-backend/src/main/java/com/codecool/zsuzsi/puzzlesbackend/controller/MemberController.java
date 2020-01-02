package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
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

    @GetMapping("/all-members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/profile")
    public Member getMyProfile() {
        return memberService.getLoggedInMember();
    }

    @PutMapping("/profile/update")
    public Member updateProfile(@RequestBody UserCredentials data) {
        return memberService.updateProfile(data);
    }

    @PutMapping("/update/{id}")
    public Member updateMember(@PathVariable("id") Long id, @RequestBody UserCredentials data) {
        return memberService.updateProfile(id, data);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
    }
}
