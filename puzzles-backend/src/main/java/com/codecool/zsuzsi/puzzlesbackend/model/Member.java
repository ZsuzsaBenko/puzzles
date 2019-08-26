package com.codecool.zsuzsi.puzzlesbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @CreationTimestamp
    private LocalDateTime registration;

    private Integer score;

    @OneToMany(mappedBy = "member")
    @EqualsAndHashCode.Exclude
    @JsonBackReference(value = "member")
    Set<Puzzle> puzzles;

    @OneToMany(mappedBy = "member")
    @EqualsAndHashCode.Exclude
    Set<Comment> comments;

    @OneToMany(mappedBy = "member")
    @EqualsAndHashCode.Exclude
    Set<Solution> solutions;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registration=" + registration +
                ", score=" + score +
                '}';
    }
}
