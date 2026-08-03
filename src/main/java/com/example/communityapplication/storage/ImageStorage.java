package com.example.communityapplication.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    //저장
    String upload(MultipartFile file, String directory);
    //삭제
    void delete(String key);
}
