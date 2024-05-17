package com.sajang.devracebackend.response;

import com.sajang.devracebackend.response.exception.exception500.AwsS3ServerException;
import com.sajang.devracebackend.response.exception.exception500.CrawlingServerException;
import com.sajang.devracebackend.response.exception.exception400.*;
import com.sajang.devracebackend.response.exception.exception404.*;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import com.sajang.devracebackend.response.responseitem.StatusItem;
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
        if (ex.getMessage().equals("For input string: \"anonymousUser\"")) {
            return ResponseData.toResponseEntity(ResponseCode.anonymousUser_ERROR);  // 시큐리티 헤더의 로그인 정보가 없을때 값을 조회하면 발생.
        }

        log.error(StatusItem.INTERNAL_SERVER_ERROR + " " + MessageItem.INTERNAL_SERVER_ERROR + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleUnauthorizedException(Exception ex) {
        return ResponseData.toResponseEntity(ResponseCode.UNAUTHORIZED_ERROR);
        // 사실상 의미가 없는게, 예외처리권한이 JwtAuthenticationEntryPoint 에게 넘어가기에 크롬콘솔에선 설정한방식대로 출력되지않는다.
        // 하지만 이는 postman 프로그램에서 출력받아 확인할 수 있다.
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleForbiddenException(Exception ex) {
        log.error(StatusItem.FORBIDDEN + " " + MessageItem.FORBIDDEN  + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.FORBIDDEN_ERROR);
        // 사실상 의미가 없는게, 예외처리권한이 JwtAuthenticationEntryPoint 에게 넘어가기에 크롬콘솔에선 설정한방식대로 출력되지않는다.
        // 하지만 이는 postman 프로그램에서 출력받아 확인할 수 있다.
    }

    @ExceptionHandler(BojIdDuplicateException.class)  // 404 Error
    public ResponseEntity handleBojIdDuplicateException(BojIdDuplicateException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data by duplicate / " + "bojId = " + ex.getBojId());
        return ResponseData.toResponseEntity(ResponseCode.DUPLICATE_BOJID);
    }

    // ===================== //

    // < 400 Exception >

    @ExceptionHandler(ChatBadRequestException.class)
    public ResponseEntity handleChatBadRequestException(ChatBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_CHAT);
    }

    @ExceptionHandler(ProblemBadRequestException.class)
    public ResponseEntity handleProblemBadRequestException(ProblemBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_PROBLEM);
    }

    @ExceptionHandler(RoomBadRequestException.class)
    public ResponseEntity handleRoomBadRequestException(RoomBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_ROOM);
    }

    @ExceptionHandler(TokenBadRequestException.class)
    public ResponseEntity handleTokenBadRequestException(TokenBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_TOKEN);
    }

    @ExceptionHandler(UserBadRequestException.class)
    public ResponseEntity handleUserBadRequestException(UserBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_USER);
    }

    @ExceptionHandler(UserRoomBadRequestException.class)
    public ResponseEntity handleUserRoomBadRequestException(UserRoomBadRequestException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_messege / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.BAD_REQUEST_USERROOM);
    }

    // ===================== //

    // < 404 Exception >

    @ExceptionHandler(NoSuchBojIdException.class)
    public ResponseEntity handleNoSuchUserException(NoSuchBojIdException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_BOJID);
    }

    @ExceptionHandler(NoSuchChatException.class)
    public ResponseEntity handleNoSuchChatException(NoSuchUserException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_CHAT);
    }

    @ExceptionHandler(NoSuchProblemException.class)
    public ResponseEntity handleNoSuchProblemException(NoSuchProblemException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_PROBLEM);
    }

    @ExceptionHandler(NoSuchRoomException.class)
    public ResponseEntity handleNoSuchRoomException(NoSuchRoomException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_ROOM);
    }

    @ExceptionHandler(NoSuchUserRoomException.class)
    public ResponseEntity handleNoSuchUserRoomException(NoSuchUserRoomException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_USERROOM);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity handleNoSuchUserException(NoSuchUserException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage() + "\n" + "==> error_data / " + ex.getMessage());
        return ResponseData.toResponseEntity(ResponseCode.NOT_FOUND_USER);
    }

    // ===================== //

    // < 500 Exception >

    @ExceptionHandler(CrawlingServerException.class)
    public ResponseEntity handleCrawlingServerException(CrawlingServerException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage());
        return ResponseData.toResponseEntity(ResponseCode.CRAWLING_ERROR);
    }

    @ExceptionHandler(AwsS3ServerException.class)
    public ResponseEntity handleAwsS3ServerException(AwsS3ServerException ex) {
        log.error(ex.getErrorStatus() + " " + ex.getErrorMessage());
        return ResponseData.toResponseEntity(ResponseCode.AWS_S3_ERROR);
    }
}
