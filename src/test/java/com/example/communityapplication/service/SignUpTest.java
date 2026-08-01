package com.example.communityapplication.service;

import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class SignUpTest {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 create 검증")
    void signUpTestPasswordEncrypt_Success() throws IllegalAccessException {
        String email = "test@test.com";
        String password = "Password123!";

        usersService.create(email, password, "testUser", "profile.jpg");

        Users user = usersRepository.findByEmail(email).orElseThrow();

        //저장된 비밀번호는 암호화
        assertTrue(
                passwordEncoder.matches(password, user.getPassword())
        );

        //실제 넣은 평문 비밀번호랑 암호화된 거 같지 않게 검증
        assertNotEquals(password, user.getPassword());

    }

    @Test
    @DisplayName("중복 이메일 가입 시도 실패 테스트")
    void signUpTestEmailDuplicate_Fail() throws IllegalAccessException{
        //회원가입 한 사용자
        usersService.create("userA@test.com", "Password123!", "testUserA", "profile.jpg");


        IllegalAccessException exception =  assertThrows(
                IllegalAccessException.class,
                ()-> {
                    usersService.create(
                            "userA@test.com",
                            "newPassword123!",
                            "testUserB",
                            "profile2.jpg"
                    );
                });

        assertEquals("signup unavailable - existing email", exception.getMessage());
    }

    @Test
    @DisplayName("중복 닉네임 가입 시도 실패 테스트")
    void signUpTestNicknameDuplicate_Fail() throws IllegalAccessException{
        //회원가입 한 사용자
        usersService.create("userA@test.com", "Password123!", "testUserA", "profile.jpg");


        IllegalAccessException exception =  assertThrows(
                IllegalAccessException.class, ()->
                    usersService.create(
                            "userB@test.com",
                            "newPassword123!",
                            "testUserA",
                            "profile2.jpg"
                    )
                );

        assertEquals("signup unavailable - existing nickname", exception.getMessage());
    }
}