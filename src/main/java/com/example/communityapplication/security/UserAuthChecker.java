package com.example.communityapplication.security;

import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userAuthChecker")
@RequiredArgsConstructor
public class UserAuthChecker {
    private final UsersRepository usersRepository;

    public boolean isOwner(Long userId, String name) {
        Optional<Users> targetUser = usersRepository.findById(userId);
        Optional<Users> user = usersRepository.findByEmail(name);

        if(targetUser.isEmpty() || user.isEmpty()){
            return false;
        }
        return user.get().getId().equals(targetUser.get().getId());
    }
}