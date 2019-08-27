package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
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

    public Member getLoggedInMember(Member member) {
        return this.memberRepository.findByEmail(member.getEmail()).orElse(null);
    }
    
    public List<Member> getLeaderBoard() {
        return memberRepository.findTop20ByOrderByScoreDesc();
    }
}
