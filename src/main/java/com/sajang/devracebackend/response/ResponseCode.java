package com.sajang.devracebackend.response;

import com.sajang.devracebackend.response.responseitem.MessageItem;
import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // Swagger API 응답값 미리보기 용도
    string(StatusItem.OK, "Swagger API"),

    // ===================== //

    // User 성공 응답
    CREATED_USER(StatusItem.CREATED, MessageItem.CREATED_USER),
    READ_USER(StatusItem.OK, MessageItem.READ_USER),
    UPDATE_USER(StatusItem.NO_CONTENT, MessageItem.UPDATE_USER),
    DELETE_USER(StatusItem.NO_CONTENT, MessageItem.DELETE_USER),

    // User 실패 응답
    NOT_FOUND_USER(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_USER),
    NOT_FOUND_BOJID(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_BOJID),
    BAD_REQUEST_USER(StatusItem.BAD_REQUEST, MessageItem.BAD_REQUEST_USER),
    DUPLICATE_BOJID(StatusItem.BAD_REQUEST, MessageItem.DUPLICATE_BOJID),

    // ===================== //

    // Room 성공 응답
    CREATED_ROOM(StatusItem.CREATED, MessageItem.CREATED_ROOM),
    READ_ROOM(StatusItem.OK, MessageItem.READ_ROOM),

    // Room 실패 응답
    NOT_FOUND_ROOM(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_ROOM),
    BAD_REQUEST_ROOM(StatusItem.BAD_REQUEST, MessageItem.BAD_REQUEST_ROOM),

    // ===================== //

    // UserRoom 성공 응답
    CREATED_USERROOM(StatusItem.CREATED, MessageItem.CREATED_USERROOM),
    READ_USERROOM(StatusItem.OK, MessageItem.READ_USERROOM),
    UPDATE_USERROOM(StatusItem.NO_CONTENT, MessageItem.UPDATE_USERROOM),

    // UserRoom 실패 응답
    NOT_FOUND_USERROOM(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_USERROOM),
    BAD_REQUEST_USERROOM(StatusItem.BAD_REQUEST, MessageItem.BAD_REQUEST_USERROOM),

    // ===================== //

    // Chat 성공 응답
    READ_CHATLIST(StatusItem.OK, MessageItem.READ_CHATLIST),

    // Chat 실패 응답
    NOT_FOUND_CHAT(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_CHAT),
    BAD_REQUEST_CHAT(StatusItem.BAD_REQUEST, MessageItem.BAD_REQUEST_CHAT),

    // ===================== //

    // Problem 성공 응답
    READ_PROBLEM(StatusItem.OK, MessageItem.READ_PROBLEM),

    // Problem 실패 응답
    NOT_FOUND_PROBLEM(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_PROBLEM),
    BAD_REQUEST_PROBLEM(StatusItem.BAD_REQUEST, MessageItem.BAD_REQUEST_PROBLEM),
    CRAWLING_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.CRAWLING_ERROR),

    // ===================== //

    // Auth 실패 응답
    UNAUTHORIZED_ERROR(StatusItem.UNAUTHORIZED, MessageItem.UNAUTHORIZED),
    FORBIDDEN_ERROR(StatusItem.FORBIDDEN, MessageItem.FORBIDDEN),

    // ===================== //

    // Token 성공 응답
    REISSUE_SUCCESS(StatusItem.OK, MessageItem.REISSUE_SUCCESS),

    // Token 실패 응답
    TOKEN_EXPIRED(StatusItem.UNAUTHORIZED, MessageItem.TOKEN_EXPIRED),
    TOKEN_ERROR(StatusItem.UNAUTHORIZED, MessageItem.TOKEN_ERROR),

    // ===================== //

    // 기타 성공 응답
    READ_DATA(StatusItem.OK, MessageItem.READ_DATA),
    READ_SOLVEDCOUNT(StatusItem.OK, MessageItem.READ_SOLVEDCOUNT),
    HEALTHY_SUCCESS(StatusItem.OK, MessageItem.HEALTHY_SUCCESS),
    TEST_SUCCESS(StatusItem.OK, MessageItem.TEST_SUCCESS),

    // 기타 실패 응답
    INTERNAL_SERVER_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.INTERNAL_SERVER_ERROR),
    anonymousUser_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.anonymousUser_ERROR),
    AWS_S3_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.AWS_S3_ERROR),

    // ===================== //
    ;

    private int httpStatus;
    private String message;
}
