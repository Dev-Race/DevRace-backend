package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public abstract class CustomException404 extends RuntimeException {

    private Integer errorStatus;
    private String errorMessage;

    private String message;

    public CustomException404(String errorMessage, String message) {
        this.errorStatus = StatusItem.NOT_FOUND;
        this.errorMessage = errorMessage;

        this.message = message;
    }
}
