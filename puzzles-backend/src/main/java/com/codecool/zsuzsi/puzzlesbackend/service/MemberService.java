package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final MemberRepository memberRepository;
    private final JwtTokenServices jwtTokenServices;

    public Member register(Member member) {
        Optional<Member> existingMember = memberRepository.findByEmail(member.getEmail());

        if (existingMember.isEmpty()) {
            Member newMember = Member.builder()
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .score(0)
                    .roles(Set.of("USER"))
                    .build();
            memberRepository.save(newMember);
            return newMember;
        } else {
            return null;
        }
    }
    
    public Member getMemberFromToken(String token) {
        token = token.substring(7);
        String email = jwtTokenServices.getEmailFromToken(token);
        return memberRepository.findByEmail(email).orElse(null);
    }

    public List<Member> getTopLeaderBoard() {
        return memberRepository.findTop10ByOrderByScoreDesc();
    }

    public List<Member> getFullLeaderBoard() {
        return memberRepository.findAllByOrderByScoreDesc();
    }
}
