package com.sajang.devracebackend.response.exception.exception404;

import com.sajang.devracebackend.response.exception.CustomException404;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import lombok.Getter;

@Getter
public class NoSuchRoomException extends CustomException404 {

    public NoSuchRoomException(String message) {
        super(MessageItem.NOT_FOUND_ROOM, message);
    }
}
