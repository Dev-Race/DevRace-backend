package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.responseitem.MessageItem;
import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public class CrawlingServerException extends RuntimeException {

    private Integer errorStatus;
    private String errorMessage;

    public CrawlingServerException() {  // 이 예외처리의 경우에는, 아무 파라미터도 받지말것.
        this.errorStatus = StatusItem.INTERNAL_SERVER_ERROR;
        this.errorMessage = MessageItem.CRAWLING_ERROR;
    }
}
