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
    public List<Puzzle> getUnsolvedPuzzleFromEachCategory() {
        Member member = memberService.getLoggedInMember();
        return puzzleService.getUnsolvedPuzzleFromEachCategory(member);
    }

    @GetMapping("/logged-in-member")
    public List<Puzzle> getAllPuzzlesByLoggedInMember() {
        Member member = memberService.getLoggedInMember();
        return puzzleService.getAllPuzzlesByMember(member);
    }

    @GetMapping("/member/{id}")
    public List<Puzzle> getAllPuzzlesByMember(@PathVariable("id") Long id) {
        return puzzleService.getAllPuzzlesByMember(id);
    }

    @GetMapping("/{category}")
    public List<Puzzle> getPuzzlesByCategory(@PathVariable("category") Category category) {
        return puzzleService.getAllPuzzlesByCategory(category);
    }

    @GetMapping("/sort/{criteria}")
    public List<Puzzle> getSortedPuzzles(@PathVariable("criteria") String criteria) {
        return puzzleService.getSortedPuzzles(criteria);
    }

    @GetMapping("/sort/{category}/{criteria}")
    public List<Puzzle> getSortedPuzzles(@PathVariable("category") Category category,
                                         @PathVariable("criteria") String criteria) {
        return puzzleService.getSortedPuzzles(category, criteria);
    }

    @GetMapping("/all/{id}")
    public Puzzle getPuzzle(@PathVariable("id") Long id) {
        return puzzleService.getById(id);
    }

    @PostMapping("/add")
    public Puzzle addNewPuzzle(@RequestBody Puzzle puzzle) {
        Member member = memberService.getLoggedInMember();
        return puzzleService.addNewPuzzle(puzzle, member);
    }

    @PutMapping("/update/{id}")
    public Puzzle updatePuzzle(@PathVariable("id") Long id, @RequestBody Puzzle updatedPuzzle) {
        return puzzleService.updatePuzzle(id, updatedPuzzle);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePuzzle(@PathVariable("id") Long id) {
        puzzleService.deletePuzzle(id);
    }
}
