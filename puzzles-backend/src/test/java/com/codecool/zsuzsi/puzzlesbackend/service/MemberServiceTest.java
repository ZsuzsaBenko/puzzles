package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.InvalidRegistrationException;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.UserCredentials;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.util.CipherMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(basePackageClasses = {MemberService.class})
@Import(CipherMaker.class)
public class MemberServiceTest {

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    private Member registeredMember;
    private String username = "name";
    private String email = "email@email.hu";
    private String password = "password";
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @BeforeEach
    public void init() {
         registeredMember = Member.builder()
                .id(1L)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .score(0)
                .build();
    }

    @Test
    public void testRegistrationIfEmailExists() {
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(registeredMember));

        UserCredentials data = UserCredentials.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        assertThrows(InvalidRegistrationException.class, () -> memberService.register(data));

        verify(memberRepository).findByEmail(email);
    }

    @Test
    public void testSuccessfulRegistration() {
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(registeredMember);

        UserCredentials data = UserCredentials.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        Member result = memberService.register(data);

        assertEquals(registeredMember.getId(), result.getId());
        assertEquals(registeredMember.getUsername(), result.getUsername());
        assertEquals(registeredMember.getEmail(), result.getEmail());
        assertEquals(registeredMember.getScore(), result.getScore());
        assertTrue(result.getPassword().startsWith("{bcrypt}"));

        verify(memberRepository).findByEmail(email);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @WithMockUser
    public void testGetLoggedInMember() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(registeredMember));

        Member result = memberService.getLoggedInMember();

        assertEquals(registeredMember, result);
        verify(memberRepository).findByEmail(anyString());
    }

    @Test
    public void testGetTopLeaderboard() {
        List<Member> members = Arrays.asList(Member.builder().username("A").score(100).build(),
                Member.builder().username("B").score(90).build(),
                Member.builder().username("C").score(80).build(),
                Member.builder().username("D").score(70).build(),
                Member.builder().username("E").score(60).build(),
                Member.builder().username("F").score(50).build(),
                Member.builder().username("G").score(40).build(),
                Member.builder().username("H").score(30).build(),
                Member.builder().username("I").score(20).build(),
                Member.builder().username("J").score(10).build());

        when(memberRepository.findTop10ByOrderByScoreDesc()).thenReturn(members);

        List<Member> result = memberService.getTopLeaderBoard();

        assertIterableEquals(members, result);

        verify(memberRepository).findTop10ByOrderByScoreDesc();
    }

    @Test
    public void testGetFullLeaderboard() {
        List<Member> members = Arrays.asList(Member.builder().username("A").score(100).build(),
                Member.builder().username("B").score(90).build(),
                Member.builder().username("C").score(80).build(),
                Member.builder().username("D").score(70).build(),
                Member.builder().username("E").score(60).build(),
                Member.builder().username("F").score(50).build(),
                Member.builder().username("G").score(40).build(),
                Member.builder().username("H").score(30).build(),
                Member.builder().username("I").score(20).build(),
                Member.builder().username("J").score(10).build(),
                Member.builder().username("J").score(5).build(),
                Member.builder().username("J").score(2).build());

        when(memberRepository.findAllByOrderByScoreDesc()).thenReturn(members);

        List<Member> result = memberService.getFullLeaderBoard();

        assertIterableEquals(members, result);

        verify(memberRepository).findAllByOrderByScoreDesc();
    }

    @Test
    @WithMockUser
    public void testUpdateUsername() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(registeredMember));

        String updatedName = "newName";
        UserCredentials data = UserCredentials.builder().username(updatedName).build();

        Member result = memberService.updateProfile(data);

        assertEquals(updatedName, result.getUsername());
        verify(memberRepository).findByEmail(anyString());
    }

    @Test
    @WithMockUser
    public void testUpdatePassword() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(registeredMember));

        String updatedPassword = "newPassword";
        UserCredentials data = UserCredentials.builder().password(updatedPassword).build();

        Member result = memberService.updateProfile(data);

        assertNotEquals(passwordEncoder.encode(password), result.getPassword());
        verify(memberRepository).findByEmail(anyString());
    }
}