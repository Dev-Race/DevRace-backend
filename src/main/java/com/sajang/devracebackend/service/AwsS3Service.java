package com.sajang.devracebackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AwsS3Service {
    String uploadImage(MultipartFile file) throws IOException;
    void deleteImage(String fileUrl);  // 사진 업로드를 위해 이전 사진을 제거하는 용도 및 차후 기본이미지로 변경시 사용 예정 메소드
}
