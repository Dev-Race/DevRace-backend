package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
}
