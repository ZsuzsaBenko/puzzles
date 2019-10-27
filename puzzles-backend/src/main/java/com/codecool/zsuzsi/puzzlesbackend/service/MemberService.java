package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.InvalidRegistrationException;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Member register(UserCredentials data) {
        this.validateData(data);

        Member newMember = Member.builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .score(0)
                .roles(Set.of("USER"))
                .build();

        log.info("New registered member with username " + newMember.getUsername() +
                " and email " + newMember.getEmail());

        return memberRepository.save(newMember);
    }

    public Member getLoggedInMember() {
        String email = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return memberRepository.findByEmail(email).get();
    }

    public List<Member> getTopLeaderBoard() {
        log.info("Leaderboard top 10 requested");
        return memberRepository.findTop10ByOrderByScoreDesc();
    }

    public List<Member> getFullLeaderBoard() {
        log.info("Full leaderboard requested");
        return memberRepository.findAllByOrderByScoreDesc();
    }

    public Member updateProfile(UserCredentials data) {
        Member loggedInMember = getLoggedInMember();
        if (data.getUsername() != null) {
            loggedInMember.setUsername(data.getUsername());
        }
        if (data.getPassword() != null) {
            loggedInMember.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        memberRepository.save(loggedInMember);

        log.info("Member " + loggedInMember.getEmail() + "'s personal data updated");

        return loggedInMember;
    }

    private void validateData(UserCredentials data) {
        if (data.getUsername() == null || data.getEmail() == null || data.getPassword() == null ||
                memberRepository.findByEmail(data.getEmail()).isPresent()) {
            log.info("Registration failed: data is incomplete or email already exists");
            throw new InvalidRegistrationException();
        }
    }
}
