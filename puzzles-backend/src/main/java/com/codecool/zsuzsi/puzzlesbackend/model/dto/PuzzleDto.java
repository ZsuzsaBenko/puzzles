package com.codecool.zsuzsi.puzzlesbackend.model.dto;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuzzleDto {

    private Long id;

    private Category category;

    private String title;

    private String instruction;

    private String puzzleItem;

    private Level level;

    private LocalDateTime submissionTime;

    private double rating;

    private MemberDto member;

    @Override
    public String toString() {
        return "Puzzle{" +
                "id=" + id +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", instruction='" + instruction + '\'' +
                ", puzzleItem='" + puzzleItem + '\'' +
                ", level=" + level +
                ", submissionTime=" + submissionTime +
                ", rating=" + rating +
                ", member=" + member +
                '}';
    }
}
