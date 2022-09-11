package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.InvalidRegistrationException;
import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.MemberNotFoundException;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
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
    private final PuzzleRepository puzzleRepository;

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
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    public Member getMemberById(Long id) {
        log.info("Member by id {} requested", id);
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getTopLeaderBoard() {
        log.info("Leaderboard top 10 requested");
        return memberRepository.findTop10ByOrderByScoreDesc();
    }

    public List<Member> getFullLeaderBoard() {
        log.info("Full leaderboard requested");
        return memberRepository.findAllByOrderByScoreDesc();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAllByOrderByRegistrationDesc();
    }

    public Member updateProfile(UserCredentials data) {
        Member loggedInMember = getLoggedInMember();
        return updateMember(loggedInMember, data);
    }

    public Member updateProfile(Long id, UserCredentials data) {
        Optional<Member> memberToUpdate = memberRepository.findById(id);
        if (memberToUpdate.isEmpty()) throw new MemberNotFoundException();

        return updateMember(memberToUpdate.get(), data);
    }

    public void deleteMember(Long id) {
        log.info("Deletion of member with id " + id + " requested");
        Optional<Member> memberToBeDeleted = memberRepository.findById(id);

        if (memberToBeDeleted.isEmpty()) throw new MemberNotFoundException();

        deleteMemberReferenceOfPuzzles(memberToBeDeleted.get());
        memberRepository.delete(memberToBeDeleted.get());

    }

    private Member updateMember(Member member, UserCredentials data) {
        if (data.getUsername() != null) {
            member.setUsername(data.getUsername());
        }
        if (data.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        memberRepository.save(member);

        log.info("Member " + member.getEmail() + "'s personal data updated");

        return member;
    }

    private void deleteMemberReferenceOfPuzzles(Member memberToBeDeleted) {
        List<Puzzle> puzzlesOfMember = puzzleRepository.findAllByMember(memberToBeDeleted);
        puzzlesOfMember.forEach(puzzle -> {
            puzzle.setMember(null);
            puzzleRepository.save(puzzle);
        });
    }

    private void validateData(UserCredentials data) {
        if (data.getUsername() == null ||
                data.getEmail() == null ||
                data.getPassword() == null ||
                memberRepository.findByEmail(data.getEmail()).isPresent() ||
                memberRepository.findByUsername(data.getUsername()).isPresent()) {
            log.info("Registration failed: data is incomplete or user already exists");
            throw new InvalidRegistrationException();
        }
    }
}
