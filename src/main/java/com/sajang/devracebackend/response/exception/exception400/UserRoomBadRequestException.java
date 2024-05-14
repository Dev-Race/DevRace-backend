package com.sajang.devracebackend.response.exception.exception400;

import com.sajang.devracebackend.response.exception.CustomException400;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import lombok.Getter;

@Getter
public class UserRoomBadRequestException extends CustomException400 {

    public UserRoomBadRequestException(String message) {
        super(MessageItem.BAD_REQUEST_USERROOM, message);
    }
}
