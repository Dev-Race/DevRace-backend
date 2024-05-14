package com.sajang.devracebackend.response.exception.exception404;

import com.sajang.devracebackend.response.exception.CustomException404;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import lombok.Getter;

@Getter
public class NoSuchProblemException extends CustomException404 {

    public NoSuchProblemException(String message) {
        super(MessageItem.NOT_FOUND_PROBLEM, message);
    }
}
