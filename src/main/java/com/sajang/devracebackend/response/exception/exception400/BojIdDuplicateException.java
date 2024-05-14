package com.sajang.devracebackend.response.exception.exception400;

import com.sajang.devracebackend.response.responseitem.MessageItem;
import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public class BojIdDuplicateException extends RuntimeException {

    private Integer errorStatus;
    private String errorMessage;

    private String bojId;

    public BojIdDuplicateException(String bojId) {  // 이 예외처리의 경우에는, message말고 bojId를 파라미터로 받을것.
        this.errorStatus = StatusItem.BAD_REQUEST;
        this.errorMessage = MessageItem.DUPLICATE_BOJID;

        this.bojId = bojId;
    }
}
