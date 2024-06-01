package com.sajang.devracebackend.response.exception;

import com.sajang.devracebackend.response.ResponseCode;
import lombok.Getter;

@Getter
public class Exception500 extends CustomException {

    public Exception500(ResponseCode errorResponseCode) {
        super(errorResponseCode, null);
    }


    public static class AwsS3Server extends Exception500 {
        public AwsS3Server() {
            super(ResponseCode.AWS_S3_ERROR);
        }
    }

    public static class CrawlingServer extends Exception500 {
        public CrawlingServer() {
            super(ResponseCode.CRAWLING_ERROR);
        }
    }
}