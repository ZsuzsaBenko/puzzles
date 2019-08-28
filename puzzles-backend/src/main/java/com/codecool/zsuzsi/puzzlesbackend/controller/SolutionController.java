package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
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

    @PostMapping("/save")
    public Solution saveSolution(@RequestBody Solution solution) {
        return solutionService.saveSolution(solution);
    }

    @PostMapping("all/member")
    public List<Solution> getAllSolutionsOfMember(@RequestBody Member member) {
        return solutionService.getAllSolutionsOfMember(member);
    }
}
