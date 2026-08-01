package com.example.communityapplication.service;

import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpdateUserInfoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //시나리오 : A가 로그인 한 상태에서 B의 비밀번호를 바꾸려고 시도 -> 실패. 오로지 자신의 비밀번호만 변경 가능
    // @WithMockUser → 로그인한 사용자의 인증 정보를 생성
    // Users → 비밀번호 변경 대상 사용자를 테스트 DB에 저장
    @Test
    @DisplayName("다른 사용자의 비밀번호 수정 시 실패 테스트")
    void updatePasswordWithOtherUser_Fail() throws Exception{
        Users userB = usersRepository.save( new Users(
                        "userB@test.com",
                        passwordEncoder.encode("userBPwd123!"),
                        "유저B",
                        "userBProfile.jpg"
                ));

        mockMvc.perform(patch("/users/{userId}/password", userB.getId()) //userB의 id를 사용해 비밀번호 변경
                        .with(user("userA@test.com")) //지금 로그인 한 유저는 userA
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "password": "NewPassword123!"
                        }
                        """))
                .andExpect(authenticated()) //userA의 로그인 된 상태인증
                .andExpect(status().isForbidden()); //권한 없음의 403 에러
    }

    @Test
    @DisplayName("자신의 계정 비밀번호 수정 성공 테스트")
    void updatePasswordWithValidUser_Success() throws Exception{
        Users userA = usersRepository.save( new Users(
                "userA@test.com",
                passwordEncoder.encode("userAPwd123!"),
                "유저A",
                "userAProfile.jpg"
        ));
        mockMvc.perform(patch("/users/{userId}/password", userA.getId())
                        .with(user(userA.getEmail()))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "password" : "newPassword123!"
                        }
                        """))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        Users loggedInUser = usersRepository.findById(userA.getId())
                .orElseThrow();
        assertTrue(
                passwordEncoder.matches("newPassword123!", loggedInUser.getPassword())
        );
    }
}
