package com.sajang.devracebackend.response;

import com.sajang.devracebackend.response.exception.CustomException;
import com.sajang.devracebackend.response.exception.Exception400;
import com.sajang.devracebackend.response.exception.Exception404;
import com.sajang.devracebackend.response.exception.Exception500;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {  // 참고로 Filter에서 throw된 에러는 보다 앞단에 위치하여 ExceptionHandler가 잡아내지 못함.

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().equals("Security Context에 인증 정보가 없습니다.")) {
            return logAndResponse(ResponseCode.anonymousUser_ERROR, ex.getMessage());  // 시큐리티 헤더의 로그인 정보가 없을때 값을 조회하면 발생.
        }
        return logAndResponse(ResponseCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleUnauthorizedException(Exception ex) {
        return logAndResponse(ResponseCode.UNAUTHORIZED_ERROR, ex.getMessage());
        // 예외처리권한이 JwtAuthenticationEntryPoint로 넘어가기에 크롬콘솔에선 설정한방식대로 출력되지않지만, 이는 postman 프로그램에서 확인이 가능하기에 명시하였음.
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleForbiddenException(Exception ex) {
        return logAndResponse(ResponseCode.FORBIDDEN_ERROR, ex.getMessage());
        // 예외처리권한이 JwtAccessDeniedHandler로 넘어가기에 크롬콘솔에선 설정한방식대로 출력되지않지만, 이는 postman 프로그램에서 확인이 가능하기에 명시하였음.
    }

    // ========== 커스텀 예외 처리 ========== //

    // < 400,404,500 Exception >
    @ExceptionHandler({
            Exception400.class,
            Exception404.class,
            Exception500.class
    })
    public ResponseEntity handleCustomExceptions(CustomException ex) {
        return logAndResponse(ex.getErrorResponseCode(), ex.getMessage());
    }

    // ========== 유틸성 메소드 ========== //

    private ResponseEntity logAndResponse(ResponseCode responseCode, String message) {
        Integer statusItem = responseCode.getHttpStatus();
        String messageItem = responseCode.getMessage();

        String prefix = (statusItem == 404) ? "==> error_data / " : "==> error_message / ";  // 404 예외처리인 경우에만, 'error_data'로 출력.
        message = prefix + message;

        log.error(statusItem + " " + messageItem + "\n" + message);
        return ResponseData.toResponseEntity(responseCode);
    }
}
