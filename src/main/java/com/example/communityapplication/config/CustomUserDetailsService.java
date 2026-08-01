package com.example.communityapplication.config;


import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    //Spring Security가 조회 기능을 사용할 수 있게 연결해주는 어댑터.
    // Users를 SecurityService가 이해할 수 있는 UserDetails로 변환해주는 작업
    private final UsersRepository userRepository;

    public CustomUserDetailsService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Spring Security의 UserDetails 객체로 변환
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList() // 우선 간단하게 권한은 비워둡니다.
        );
    }
}
