package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.ResponseCode;
import lombok.Getter;

@Getter
public class Exception400 extends CustomException {

    public Exception400(ResponseCode errorResponseCode, String message) {
        super(errorResponseCode, message);
    }


    public static class BojIdDuplicate extends Exception400 {
        public BojIdDuplicate(String message) {
            super(ResponseCode.DUPLICATE_BOJID, "duplicate " + message);
        }
    }

    public static class UserBadRequest extends Exception400 {
        public UserBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_USER, message);
        }
    }

    public static class RoomBadRequest extends Exception400 {
        public RoomBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_ROOM, message);
        }
    }

    public static class UserRoomBadRequest extends Exception400 {
        public UserRoomBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_USERROOM, message);
        }
    }

    public static class ProblemBadRequest extends Exception400 {
        public ProblemBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_PROBLEM, message);
        }
    }

    public static class ChatBadRequest extends Exception400 {
        public ChatBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_CHAT, message);
        }
    }

    public static class TokenBadRequest extends Exception400 {
        public TokenBadRequest(String message) {
            super(ResponseCode.BAD_REQUEST_TOKEN, message);
        }
    }
}