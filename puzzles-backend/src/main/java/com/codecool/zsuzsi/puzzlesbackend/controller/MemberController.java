package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.MemberDto;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/top-leaderboard")
    public List<MemberDto> getTopLeaderBoard() {
        return memberService.getTopLeaderBoard().stream().map(this::convertMember).collect(Collectors.toList());
    }

    @GetMapping("/full-leaderboard")
    public List<MemberDto> getFullLeaderBoard() {
        return memberService.getFullLeaderBoard().stream().map(this::convertMember).collect(Collectors.toList());
    }

    @GetMapping("/all-members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/profile")
    public Member getLoggedInMemberProfile() {
        return memberService.getLoggedInMember();
    }

    @PutMapping("/profile/update")
    public Member updateLoggedInMemberProfile(@RequestBody UserCredentials data) {
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

    private MemberDto convertMember(Member member) {
        return modelMapper.map(member, MemberDto.class);
    }

}
