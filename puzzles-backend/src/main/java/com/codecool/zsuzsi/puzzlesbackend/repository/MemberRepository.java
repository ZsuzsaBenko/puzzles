package com.codecool.zsuzsi.puzzlesbackend.repository;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository {

    Optional<Member> findByEmail(String email);
}
