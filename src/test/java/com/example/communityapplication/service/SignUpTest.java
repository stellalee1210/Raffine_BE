package com.example.communityapplication.service;

import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private String email;
    private String password;
    private MultipartFile profilePicture;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        email = "test@test.com";
        password = "Password123!";
        profilePicture = createTestImage("profile.png");

        usersService.create(
                email,
                password,
                "testUser",
                profilePicture
        );
    }

    private MultipartFile createTestImage(String filename) {
        return new MockMultipartFile(
                "profilePicture",
                filename,
                "image/png",
                "fake-image-content".getBytes()
        );
    }

    @Test
    @DisplayName("회원가입 create 검증")
    void signUpTestPasswordEncrypt_Success() throws IllegalAccessException {
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
    void signUpTestEmailDuplicate_Fail() throws IllegalArgumentException{
        MultipartFile tempFile = createTestImage("profile2.png");


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()-> {
                    usersService.create(
                            "test@test.com",
                            "newPassword123!",
                            "testUserB",
                            tempFile
                    );
                });

        assertEquals("signup unavailable - existing email", exception.getMessage());
    }

    @Test
    @DisplayName("중복 닉네임 가입 시도 실패 테스트")
    void signUpTestNicknameDuplicate_Fail() throws IllegalArgumentException{
        //회원가입 한 사용자
        MultipartFile tempProfile = createTestImage("profile.png");


        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class, ()->
                    usersService.create(
                            "userB@test.com",
                            "newPassword123!",
                            "testUser",
                            tempProfile
                    )
                );

        assertEquals("signup unavailable - existing nickname", exception.getMessage());
    }
}