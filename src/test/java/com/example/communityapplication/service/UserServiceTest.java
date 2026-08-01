package com.example.communityapplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//Mockito 활성화하는 기능
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock //가짜 객체 생성
    private UsersRepository usersRepository;
    @InjectMocks //가짜 객체 주입
    private UsersService usersService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Users user;

    @BeforeEach
    void setUp(){
        user = new Users("test@test.com", "oldPassword", "testuser","image.jpg");

    }

    @Test
    @DisplayName("User Service Test - updateNickname 비즈니스 로직 단위 테스트")
    void updateNicknameTest_Success(){
        //given
        String newNickname = "newName";
        //updateNickname 함수 내부에 보면 repository 조회를 하는 로직이 있다. 그 때의 반환값을 설정해주는 역할
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        //when
        usersService.updateNickname( 1L , newNickname);

        //then
        assertEquals(newNickname, user.getNickname());
    }

    @Test
    @DisplayName("User Service Test - updatePassword 비즈니스 로직 단위 테스트")
    void updatePasswordTest_Success(){
        //given
        String newPassword = "newPassword";
        String encodedPassword = "eNcOdEdPaSsWoRd";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        //updateNickname 함수 내부에 보면 repository 조회를 하는 로직이 있다. 그 때의 반환값을 설정해주는 역할
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        usersService.updatePassword( 1L , newPassword);
        //then
        assertEquals(encodedPassword, user.getPassword());
    }

    @Test
    @DisplayName("User Service Test - updateProfilePicture 비즈니스 로직 단위 테스트")
    void updateProfilePictureTest_Success(){
        //given
        String newProfilePicture = "new_image.jpg";
        //updateNickname 함수 내부에 보면 repository 조회를 하는 로직이 있다. 그 때의 반환값을 설정해주는 역할
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        usersService.updateProfilePicture( 1L , newProfilePicture);

        //then
        assertEquals(newProfilePicture, user.getProfilePicture());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 실패 테스트")
    void unknownUserGetTest_Fail() throws IllegalArgumentException {
        when(usersRepository.findById(10L))
                .thenReturn(Optional.empty());
        Long tempUserId = 10L;
        IllegalArgumentException illegalArgumentException =  assertThrows(IllegalArgumentException.class, () -> usersService.getUser(tempUserId));
        assertEquals("user not found from repository by id", illegalArgumentException.getMessage());
    }
}
