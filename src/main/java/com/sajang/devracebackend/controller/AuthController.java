package com.sajang.devracebackend.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")  // 차후 SecurityConfig에 대신 만들어줄 예정.
@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
}
