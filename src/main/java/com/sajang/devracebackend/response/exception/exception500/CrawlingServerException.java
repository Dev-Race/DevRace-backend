package com.sajang.devracebackend.response.exception.exception500;

import com.sajang.devracebackend.response.exception.CustomException500;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import com.sajang.devracebackend.response.responseitem.StatusItem;
import lombok.Getter;

@Getter
public class CrawlingServerException extends CustomException500 {

    public CrawlingServerException() {
        super(MessageItem.CRAWLING_ERROR);
    }
}
