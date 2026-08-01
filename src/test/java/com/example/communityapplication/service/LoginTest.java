package com.example.communityapplication.service;

import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc //컨트롤러 계층에서 자동으로 MockMVC 주입 -> 실행 없이 응답 모킹 가능
@Transactional
public class LoginTest {

    @Autowired //자동으로 의존관계 주입
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Users user;

    //테스트 시작 전 준비 단계
    @BeforeEach
    void setUp() {
        //Users 리턴해서 user에 저장
        user = usersRepository.save(new Users(
                "test@test.com",
                passwordEncoder.encode("Test234!"),
                "테스트유저",
                "image.jpg"
        ));
    }

    @Test
    @DisplayName("유효한 로그인 성공 테스트")
    void loginWithValidCredentials_Success() throws Exception {

        // mockMvc.perform : 가상의 HTTP 요청 전송 -> 필터 체인 동작 검증
        mockMvc.perform(post("/login-process")
                        .with(csrf()) //유효한 csrf 토큰 추가
                        //contentType : 요청 본문의 데이터 형식을 서버에 알림 -> 체이닝 형식으로 엮음
                        //MediaType : HTTP 요청/응답 시 주고 받는 데이터의 형식
                        // APPLICATION_FORM_URLENCODED ; application/x-www-form-urlencoded 요청을 테스트하기 위함 ( formLogin 형식)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test@test.com") //체이닝 방식으로 본문 구성
                        .param("password", "Test234!"))
                //authenticated() : 인증 확인
                .andExpect(authenticated()
                        .withUsername("test@test.com")) //username -> 여기서는 email로 저장한 부분이 입력값과 같은지 확인
                .andExpect(cookie().value("userId", user.getId().toString())) //저장된 쿠키 중 userId가 제대로 저장됐는지 확인
                .andExpect(status().is3xxRedirection()) //로그인 성공 시 리다이렉트 응답 확인
                .andExpect(redirectedUrl(
                        "/board"
                )); //페이지 이동 목적지 검증
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 로그인 실패 테스트")
    void loginWithWrongPassword_Fail() throws Exception {
        mockMvc.perform(post("/login-process")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test@test.com")
                        .param("password", "test1234"))
                .andExpect(unauthenticated()) //unauthenticated : 인증 실패 검증
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("유효하지 않은 이메일 로그인 실패 테스트")
    void loginWithUnknownEmail_Fail() throws Exception {
        mockMvc.perform(post("/login-process")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "unknown@test.com")
                        .param("password", "tempPassword"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }
}
