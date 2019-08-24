package com.codecool.zsuzsi.puzzlesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 1024)
    private String message;

    @CreationTimestamp
    private LocalDateTime submissionTime;

    @ManyToOne
    private Puzzle puzzle;

    @ManyToOne
    private Member member;
}
