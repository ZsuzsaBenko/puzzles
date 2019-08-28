package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
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

    @GetMapping("/all")
    public List<Puzzle> getPuzzles() {
        return puzzleService.getAllPuzzles();
    }

    @PostMapping("/random")
    public List<Puzzle> getUnsolvedPuzzleFromEachCategory(@RequestBody Member member) {
        return puzzleService.getUnsolvedPuzzleFromEachCategory(member);
    }

    @PostMapping("/member")
    public List<Puzzle> getAllPuzzlesOfMember(@RequestBody  Member member) {
        return puzzleService.getAllPuzzlesOfMember(member);
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
    public Puzzle addNewPuzzle(@RequestBody Puzzle puzzle) {
        return puzzleService.addNewPuzzle(puzzle);
    }
}
