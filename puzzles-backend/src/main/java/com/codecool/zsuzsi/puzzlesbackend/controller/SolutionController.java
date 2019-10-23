package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solutions")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class SolutionController {

    private final SolutionService solutionService;
    private final MemberService memberService;

    @GetMapping("/member")
    public List<Solution> getAllSolutionsByMember() {
        Member member = memberService.getLoggedInMember();
        return solutionService.getAllSolutionsByMember(member);
    }

    @PostMapping("/save")
    public Solution saveSolution(@RequestBody Solution solution) {
        Member member = memberService.getLoggedInMember();
        return solutionService.saveSolution(solution, member);
    }
}
