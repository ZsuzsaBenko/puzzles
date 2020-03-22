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
public class CommentDto {

    private Long id;

    private String message;

    private LocalDateTime submissionTime;

    private PuzzleDto puzzle;

    private MemberDto member;
}
