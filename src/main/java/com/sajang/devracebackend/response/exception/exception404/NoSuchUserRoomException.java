package com.sajang.devracebackend.response.exception.exception404;

import com.sajang.devracebackend.response.exception.CustomException404;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import lombok.Getter;

@Getter
public class NoSuchUserRoomException extends CustomException404 {

    public NoSuchUserRoomException(String message) {
        super(MessageItem.NOT_FOUND_PROBLEM, message);
    }
}
