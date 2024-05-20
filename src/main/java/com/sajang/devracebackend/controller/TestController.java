package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.AwsS3Service;
import com.sajang.devracebackend.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final AwsS3Service awsS3Service;
    private final ProblemService problemService;


    @GetMapping("/health")
    @Operation(summary = "서버 헬스체크 [jwt X]")
    public ResponseEntity<ResponseData> healthCheck() {
        return ResponseData.toResponseEntity(ResponseCode.HEALTHY_SUCCESS);
    }


    // ========== Test 임시 용도 ========== //
//    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity uploadImage(@RequestPart(value="imageFile", required = false) MultipartFile imageFile) throws IOException {
//        String fileUrl = awsS3Service.uploadImage(imageFile);
//        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, fileUrl);
//    }
//
//    @GetMapping("/image/delete")
//    public ResponseEntity deleteImage() {
//        awsS3Service.deleteImage("Test imagefile Url");
//        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS);
//    }
//
//    @PostMapping("/crawling/{number}")
//    public ResponseEntity crawlAndSaveProblem(@PathVariable Integer number) throws IOException {
//        Problem problem = problemService.crawlAndSaveProblem(number);
//        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, problem.getProblemLimit());
//    }
}
