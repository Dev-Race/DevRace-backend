package com.sajang.devracebackend.security.jwt.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class JwtStompExceptionHandler extends StompSubProtocolErrorHandler {  // 소켓 통신 중, 예외가 발생했을 때 여기로 제어권이 넘어옴.

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        if(ex.getMessage().equals("토큰 만료 - ExpiredJwtException")) {
            return errorMessage("토큰 만료 - ExpiredJwtException");  // reissue 요망.
        }
        else if(ex.getMessage().equals("잘못된 토큰 - JwtException")) {
            return errorMessage("잘못된 토큰 - JwtException");  // 재로그인 요망.
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);  // 이외의 다른 예외들은 StompSubProtocolErrorHandler의 기본 처리 방식을 따름.
    }

    private Message<byte[]> errorMessage(String errorMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}