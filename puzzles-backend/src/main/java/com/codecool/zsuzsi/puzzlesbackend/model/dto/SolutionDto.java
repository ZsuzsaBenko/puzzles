package com.codecool.zsuzsi.puzzlesbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionDto {

    private Long id;

    private LocalDateTime submissionTime;

    private Integer seconds;

    private Integer rating;

    private PuzzleDto puzzle;

    private MemberDto member;

}
