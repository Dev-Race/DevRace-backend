package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public abstract class CustomException400 extends RuntimeException {

    private Integer errorStatus;
    private String errorMessage;

    private String message;

    public CustomException400(String errorMessage, String message) {
        this.errorStatus = StatusItem.BAD_REQUEST;
        this.errorMessage = errorMessage;

        this.message = message;
    }
}
