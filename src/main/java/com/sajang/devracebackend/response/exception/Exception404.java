package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.ResponseCode;
import lombok.Getter;

@Getter
public class Exception404 extends CustomException {

    public Exception404(ResponseCode errorResponseCode, String message) {
        super(errorResponseCode, message);
    }


    public static class NoSuchBojId extends Exception404 {
        public NoSuchBojId(String message) {
            super(ResponseCode.NOT_FOUND_BOJID, message);
        }
    }

    public static class NoSuchUser extends Exception404 {
        public NoSuchUser(String message) {
            super(ResponseCode.NOT_FOUND_USER, message);
        }
    }

    public static class NoSuchRoom extends Exception404 {
        public NoSuchRoom(String message) {
            super(ResponseCode.NOT_FOUND_ROOM, message);
        }
    }

    public static class NoSuchUserRoom extends Exception404 {
        public NoSuchUserRoom(String message) {
            super(ResponseCode.NOT_FOUND_PROBLEM, message);
        }
    }

    public static class NoSuchProblem extends Exception404 {
        public NoSuchProblem(String message) {
            super(ResponseCode.NOT_FOUND_PROBLEM, message);
        }
    }

    public static class NoSuchChat extends Exception404 {
        public NoSuchChat(String message) {
            super(ResponseCode.NOT_FOUND_CHAT, message);
        }
    }
}