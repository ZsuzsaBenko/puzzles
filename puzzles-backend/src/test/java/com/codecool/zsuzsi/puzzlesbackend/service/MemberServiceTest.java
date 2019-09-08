package com.codecool.zsuzsi.puzzlesbackend.service;

import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.security.JwtTokenServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = {MemberService.class})
@ActiveProfiles("test")
class MemberServiceTest {

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    JwtTokenServices jwtTokenServices;

    @Autowired
    MemberService memberService;

    @Test
    public void testSuccessfulRegistration() {
        System.out.println("I'm working.");
    }

}