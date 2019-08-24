package com.codecool.zsuzsi.puzzlesbackend.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Puzzle {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1024)
    private String instruction;

    @Column(nullable = false, length = 1024)
    private String puzzleItem;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Level level;

    @CreationTimestamp
    private LocalDateTime dateTime;

    private double rating = 0;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "puzzle")
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;

    @OneToMany(mappedBy = "puzzle")
    @EqualsAndHashCode.Exclude
    private Set<Solution> solutions;

    @Override
    public String toString() {
        return "Puzzle{" +
                "id=" + id +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", instruction='" + instruction + '\'' +
                ", puzzleItem='" + puzzleItem + '\'' +
                ", answer='" + answer + '\'' +
                ", level=" + level +
                ", dateTime=" + dateTime +
                ", rating=" + rating +
                ", member=" + member +
                '}';
    }
}
