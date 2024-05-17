package com.sajang.devracebackend.response.exception.exception400;

import com.sajang.devracebackend.response.exception.CustomException400;
import com.sajang.devracebackend.response.responseitem.MessageItem;

public class TokenBadRequestException extends CustomException400 {

    public TokenBadRequestException(String message) {
        super(MessageItem.BAD_REQUEST_TOKEN, message);
    }
}
