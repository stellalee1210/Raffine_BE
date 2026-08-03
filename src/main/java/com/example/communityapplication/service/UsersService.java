package com.example.communityapplication.service;

import com.example.communityapplication.dto.UserResponseDto;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;

import com.example.communityapplication.storage.LocalImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@Validated
@RequiredArgsConstructor
@Transactional
public class UsersService {
    private final UsersRepository usersRepository;
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;
    private final LocalImageStorage localImageStorage;

    public UserResponseDto create(String email, String password, String nickname, MultipartFile rawProfilePicture) throws IllegalArgumentException {
        if(usersRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("signup unavailable - existing email");
        }
        if(usersRepository.findByNickname(nickname).isPresent()){
            throw new IllegalArgumentException("signup unavailable - existing nickname");
        }

        String encryptedPassword = passwordEncoder.encode(password);
        String profilePicture = localImageStorage.upload(rawProfilePicture, "profile");
        Users user = new Users(email, encryptedPassword, nickname, profilePicture);
        Users savedUser = usersRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found from repository by id"));
        if(user.getEmail().isBlank()) throw new IllegalArgumentException("user not found - deleted user");

        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#email == authentication.name")
    public Resource getProfilePictureResource(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return localImageStorage.get(user.getProfilePicture());
    }

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    public void updateNickname(Long userId, String newNickname){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        user.changeNickname(newNickname);
    }

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    public void updatePassword(Long userId,  String newPassword){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encryptedPassword);
    }

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    public void updateProfilePicture( Long userId,  String newProfilePicture){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user profile picture not found"));
        user.changeProfilePicture(newProfilePicture);
    }

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    public void deleteUser(Long userId){
        postService.deleteAllPostFromUser(userId);
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found - cannot delete user"));
        usersRepository.delete(user);
    }
}
