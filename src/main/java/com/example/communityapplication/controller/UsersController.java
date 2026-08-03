package com.example.communityapplication.controller;

import com.example.communityapplication.dto.*;
import com.example.communityapplication.response.ApiResponse;
import com.example.communityapplication.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {
    private final UsersService usersService;

    @PostMapping(
            value = "/signup",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestPart("request") UserRequestDto request,
            @RequestPart("profilePicture") MultipartFile profilePicture
    ) {
        UserResponseDto userResponseDto;
        try {
            userResponseDto = usersService.create(request.getEmail(), request.getPassword(),request.getNickname(), profilePicture);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of("signup_success", userResponseDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long userId){
        UserResponseDto userResponse =  usersService.getUser(userId);
        return ResponseEntity
                .ok(ApiResponse.of("get_data_success", userResponse));
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<Resource> getProfilePicture ( Authentication authentication) throws IOException {
        Resource resource =
                usersService.getProfilePictureResource(authentication.getName());

        Path imagePath = resource.getFile().toPath();
        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @PatchMapping("/{userId}/nickname")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> updateNickname(@PathVariable Long userId, @Valid  @RequestBody UserUpdateRequestDto request){
        usersService.updateNickname(userId, request.getNickname());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> updatePassword(@PathVariable Long userId, @Valid  @RequestBody UserUpdateRequestDto request){
        usersService.updatePassword(userId, request.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/profilePicture")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> updateProfilePicture(@PathVariable Long userId, @Valid  @RequestBody UserUpdateRequestDto request){
        usersService.updateProfilePicture(userId, request.getProfilePicture());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> deleteUser(@PathVariable Long userId){
        usersService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}