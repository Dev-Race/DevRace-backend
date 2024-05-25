package com.sajang.devracebackend.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    private static final String SPLIT_CHAR = ",";  // 구분자 설정

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {  // 백엔드의 List를 String으로 변환하여 DB에 전달하는 메소드 (참고로 []괄호나 ""따옴표는 제외한채로.)
        if (attribute == null || attribute.size() == 0) {
            return "__null__";  // 빈 리스트를 "__null__"로 변환하여 명시.
        }
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SPLIT_CHAR));  // else 경우
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {  // DB의 String을 List로 변환하여 백엔드에 전달하는 메소드
        if (dbData == null || dbData.equals("__null__")) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(Long::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));  // else 경우
    }
}
