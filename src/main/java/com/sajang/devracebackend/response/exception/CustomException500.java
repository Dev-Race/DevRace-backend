package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public class CustomException500 extends RuntimeException {

    private Integer errorStatus;
    private String errorMessage;

    public CustomException500(String errorMessage) {
        this.errorStatus = StatusItem.INTERNAL_SERVER_ERROR;
        this.errorMessage = errorMessage;
    }
}
