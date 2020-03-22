package com.codecool.zsuzsi.puzzlesbackend.controller;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.dto.PuzzleDto;
import com.codecool.zsuzsi.puzzlesbackend.service.MemberService;
import com.codecool.zsuzsi.puzzlesbackend.service.PuzzleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/puzzles")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    public List<PuzzleDto> getAllPuzzles() {
        return puzzleService.getAllPuzzles().stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/random")
    public List<PuzzleDto> getUnsolvedPuzzleFromEachCategory() {
        Member member = memberService.getLoggedInMember();
        return puzzleService.getUnsolvedPuzzleFromEachCategory(member).stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/logged-in-member")
    public List<PuzzleDto> getAllPuzzlesByLoggedInMember() {
        Member member = memberService.getLoggedInMember();
        return puzzleService.getAllPuzzlesByMember(member).stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/member/{id}")
    public List<Puzzle> getAllPuzzlesByMember(@PathVariable("id") Long id) {
        return puzzleService.getAllPuzzlesByMember(id);
    }

    @GetMapping("/{category}")
    public List<PuzzleDto> getPuzzlesByCategory(@PathVariable("category") Category category) {
        return puzzleService.getAllPuzzlesByCategory(category).stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/sort/{criteria}")
    public List<PuzzleDto> getSortedPuzzles(@PathVariable("criteria") String criteria) {
        return puzzleService.getSortedPuzzles(criteria).stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/sort/{category}/{criteria}")
    public List<PuzzleDto> getSortedPuzzles(@PathVariable("category") Category category,
                                         @PathVariable("criteria") String criteria) {
        return puzzleService.getSortedPuzzles(category, criteria).stream().map(this::convertPuzzle).collect(Collectors.toList());
    }

    @GetMapping("/all/{id}/admin")
    public Puzzle getPuzzleForAdmin(@PathVariable("id") Long id) {
        return puzzleService.getById(id);
    }

    @GetMapping("/all/{id}")
    public PuzzleDto getPuzzle(@PathVariable("id") Long id) {
        return this.convertPuzzle(puzzleService.getById(id));
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<Boolean> checkAnswer(@PathVariable("id") Long id, @RequestBody String answer) {
        return ResponseEntity.ok(puzzleService.checkAnswer(id, answer));
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

    private PuzzleDto convertPuzzle(Puzzle puzzle) {
        return this.modelMapper.map(puzzle, PuzzleDto.class);
    }
}
