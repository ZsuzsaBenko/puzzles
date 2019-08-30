package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.PuzzleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/puzzles")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final MemberService memberService;

    @GetMapping("/all")
    public List<Puzzle> getAllPuzzles() {
        return puzzleService.getAllPuzzles();
    }

    @GetMapping("/random")
    public List<Puzzle> getUnsolvedPuzzleFromEachCategory(@RequestHeader("Authorization") String token) {
        Member member = memberService.getMemberFromToken(token);
        return puzzleService.getUnsolvedPuzzleFromEachCategory(member);
    }

    @GetMapping("/member")
    public List<Puzzle> getAllPuzzlesByMember(@RequestHeader("Authorization") String token) {
        Member member = memberService.getMemberFromToken(token);
        return puzzleService.getAllPuzzlesByMember(member);
    }

    @GetMapping("/{category}")
    public List<Puzzle> getPuzzlesByCategory(@PathVariable("category") Category category) {
        return puzzleService.getAllPuzzlesByCategory(category);
    }

    @GetMapping("/all/{id}")
    public Puzzle getPuzzle(@PathVariable("id") Long id) {
        return puzzleService.getById(id);
    }

    @GetMapping("/sort/{criteria}")
    public List<Puzzle> getSortedPuzzles(@PathVariable("criteria") String criteria) {
        return puzzleService.getSortedPuzzles(criteria);
    }

    @PostMapping("/add")
    public Puzzle addNewPuzzle(@RequestBody Puzzle puzzle,
                               @RequestHeader("Authorization") String token) {
        Member member = memberService.getMemberFromToken(token);
        return puzzleService.addNewPuzzle(puzzle, member);
    }
}
