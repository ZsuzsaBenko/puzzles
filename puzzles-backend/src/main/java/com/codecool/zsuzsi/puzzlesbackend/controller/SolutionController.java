package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Solution;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.SolutionDto;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solutions")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class SolutionController {

    private final SolutionService solutionService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/logged-in-member")
    public List<SolutionDto> getAllSolutionsByLoggedInMember() {
        Member member = memberService.getLoggedInMember();
        return solutionService.getAllSolutionsByMember(member).stream().map(this::convertSolution).collect(Collectors.toList());
    }

    @GetMapping("/member/{id}")
    public List<Solution> getAllSolutionsByMember(@PathVariable("id") Long id) {
        return solutionService.getAllSolutionsByMember(id);
    }

    @PostMapping("/save")
    public SolutionDto saveSolution(@RequestBody Solution solution) {
        Member member = memberService.getLoggedInMember();
        return convertSolution(solutionService.saveSolution(solution, member));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSolution(@PathVariable("id") Long id) {
        solutionService.deleteSolution(id);
    }

    private SolutionDto convertSolution(Solution solution) {
        return this.modelMapper.map(solution, SolutionDto.class);
    }
}
