package com.sajang.devracebackend.response.exception.exception500;

import com.sajang.devracebackend.response.exception.CustomException500;
import com.sajang.devracebackend.response.responseitem.MessageItem;
import lombok.Getter;

@Getter
public class AwsS3ServerException extends CustomException500 {

    public AwsS3ServerException() {
        super(MessageItem.AWS_S3_ERROR);
    }
}
