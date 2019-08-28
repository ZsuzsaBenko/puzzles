package com.codecool.zsuzsi.puzzlesbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Solution {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime submissionTime;

    @Column(nullable = false)
    private Integer seconds;

    private Integer rating = 0;

    @ManyToOne
    private Puzzle puzzle;

    @ManyToOne
    private Member member;

    @Transient
    private Integer timeLevel;

}
