package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @Hidden
@Tag(name = "Test")
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/health")
    @Operation(summary = "서버 헬스체크 [jwt X]")
    public ResponseEntity<ResponseData> healthCheck() {
        return ResponseData.toResponseEntity(ResponseCode.HEALTHY_SUCCESS);
    }
}
