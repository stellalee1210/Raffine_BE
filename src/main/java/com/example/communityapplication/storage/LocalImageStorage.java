package com.example.communityapplication.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Component
public class LocalImageStorage implements ImageStorage{
    private final Path uploadRoot;

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png");

    private static final long MAX_FILE_SIZE =
            5L * 1024 * 1024;

    private void validate (MultipartFile imageFile){
        //이미지 파일이 비어있는지 검증
        if(imageFile == null || imageFile.isEmpty()){
            throw new IllegalArgumentException(
                    "error : 이미지 파일이 비어있습니다."
            );
        }

        //파일 형식 검증
        String contentType = imageFile.getContentType();
        if(contentType == null
        || !ALLOWED_CONTENT_TYPES.contains(contentType)){
            throw new IllegalArgumentException(
                    "JPEG 또는 PNG 이미지만 업로드할 수 있습니다."
            );
        }

        if(imageFile.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException(
                    "이미지는 5MB 이하만 업로드할 수 있습니다."
            );
        }
    }

    private String extractExtension(String filename){
        if (filename == null
                || filename.isBlank()) {
            throw new IllegalArgumentException(
                    "파일명이 없습니다."
            );
        }

        int extensionIndex = filename.lastIndexOf('.');

        if (extensionIndex < 0) {
            throw new IllegalArgumentException(
                    "파일 확장자가 없습니다."
            );
        }

        String extension = filename
                .substring(extensionIndex)
                .toLowerCase();

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")) {
            throw new IllegalArgumentException(
                    "지원하지 않는 이미지 확장자입니다."
            );
        }

        return extension;
    }

    public LocalImageStorage(@Value("${app.upload-dir}") String uploadDir){
        this.uploadRoot = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public String upload(MultipartFile file, String directory){
        //들어온 사진파일 값 검증
        validate(file);

        //확장자 얻기
        String extension = extractExtension(file.getOriginalFilename());

        //UUID
        String storedFilename = UUID.randomUUID() + extension;

        Path targetDirectory = uploadRoot
                .resolve(directory)
                .normalize();

        Path targetPath = targetDirectory
                .resolve(storedFilename)
                .normalize();

        if (!targetPath.startsWith(uploadRoot)) {
            throw new IllegalArgumentException(
                    "잘못된 이미지 저장 경로입니다."
            );
        }

        try {
            Files.createDirectories(targetDirectory);
            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "이미지 저장에 실패했습니다.",
                    exception
            );
        }

        return directory + "/" + storedFilename;
    }


    @Override
    public Resource get(String key) throws IllegalArgumentException{

        Path imagePath = uploadRoot.resolve(key).normalize();

        if (!imagePath.startsWith(uploadRoot)) {
            throw new IllegalArgumentException(
                    "잘못된 이미지 경로입니다."
            );
        }

        try {
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists()
                    || !resource.isReadable()) {
                throw new IllegalArgumentException(
                        "이미지를 찾을 수 없습니다."
                );
            }

            return resource;

        } catch (MalformedURLException exception) {
            throw new IllegalStateException(
                    "이미지 경로를 읽을 수 없습니다.",
                    exception
            );
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        Path imagePath = uploadRoot
                .resolve(key)
                .normalize();

        if (!imagePath.startsWith(uploadRoot)) {
            throw new IllegalArgumentException(
                    "잘못된 이미지 삭제 경로입니다."
            );
        }

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "이미지 삭제에 실패했습니다.",
                    exception
            );
        }
    }
}
