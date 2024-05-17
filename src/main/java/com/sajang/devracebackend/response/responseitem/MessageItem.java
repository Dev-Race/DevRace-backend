package com.sajang.devracebackend.response.responseitem;

public class MessageItem {

    // < User >
    public static final String CREATED_USER = "SUCCESS - 회원 가입 성공";
    public static final String READ_USER = "SUCCESS - 회원 정보 조회 성공";
    public static final String UPDATE_USER = "SUCCESS - 회원 정보 수정 성공";
    public static final String DELETE_USER = "SUCCESS - 회원 탈퇴 성공";

    public static final String NOT_FOUND_USER = "ERROR - 존재하지 않는 회원 조회 에러";
    public static final String NOT_FOUND_BOJID = "ERROR - solvedac에 존재하지 않는 백준id 에러";  // 404 에러 처리.
    public static final String BAD_REQUEST_USER = "ERROR - 잘못된 회원 요청 에러";
    public static final String DUPLICATE_BOJID = "ERROR - 백준id 중복 에러";  // 400 에러 처리.

    // < Room >
    public static final String CREATED_ROOM = "SUCCESS - 방 생성 성공";
    public static final String READ_ROOM = "SUCCESS - 방 조회 성공";

    public static final String NOT_FOUND_ROOM = "ERROR - 존재하지 않는 방 조회 에러";
    public static final String BAD_REQUEST_ROOM = "ERROR - 잘못된 방 요청 에러";

    // < UserRoom >
    public static final String CREATED_USERROOM = "SUCCESS - 방 입장 성공";
    public static final String READ_USERROOM = "SUCCESS - 회원&방 조회 성공";
    public static final String UPDATE_USERROOM = "SUCCESS - 문제풀이 완료";  // 문제풀이 '실패 or 성공' 모두 해당.

    public static final String NOT_FOUND_USERROOM = "ERROR - 존재하지 않는 회원&방 조회 에러";
    public static final String BAD_REQUEST_USERROOM = "ERROR - 잘못된 회원&방 요청 에러";

    // < Chat >
    public static final String READ_CHATLIST = "SUCCESS - 채팅방의 채팅 목록 조회 성공";

    public static final String NOT_FOUND_CHAT = "ERROR - 존재하지 않는 채팅 조회 에러";
    public static final String BAD_REQUEST_CHAT = "ERROR - 잘못된 채팅 요청 에러";

    // < Problem >
    public static final String READ_PROBLEM = "SUCCESS - 문제 생성 성공";

    public static final String NOT_FOUND_PROBLEM = "ERROR - 존재하지 않는 문제 조회 에러";
    public static final String BAD_REQUEST_PROBLEM = "ERROR - 잘못된 문제 요청 에러";
    public static final String CRAWLING_ERROR = "ERROR - 문제 크롤링 에러";  // 500 에러 처리.

    // < Auth >
    public static final String UNAUTHORIZED = "ERROR - Unauthorized 에러";
    public static final String FORBIDDEN = "ERROR - Forbidden 에러";

    // < Token >
    public static final String REISSUE_SUCCESS = "SUCCESS - JWT Access 토큰 재발급 성공";

    public static final String TOKEN_EXPIRED = "ERROR - JWT 토큰 만료 에러";
    public static final String TOKEN_ERROR = "ERROR - 잘못된 JWT 토큰 에러";

    // < Etc >
    public static final String READ_DATA = "SUCCESS - 데이터 통합 조회 성공";  // 문제풀이 페이지 내 데이터들 조회 결과 응답시 사용할 예정.
    public static final String READ_SOLVEDCOUNT = "SUCCESS - 백준 solvedCount 조회 성공";
    public static final String HEALTHY_SUCCESS = "SUCCESS - Health check 성공";
    public static final String TEST_SUCCESS = "SUCCESS - Test 성공";  // Test 임시 용도

    public static final String INTERNAL_SERVER_ERROR = "ERROR - 서버 내부 에러";
    public static final String anonymousUser_ERROR = "ERROR - anonymousUser 에러";  // 시큐리티 헤더의 로그인 정보가 없을때 값을 조회하면 발생.
    public static final String AWS_S3_ERROR = "ERROR - AWS S3 서버 에러";
}
