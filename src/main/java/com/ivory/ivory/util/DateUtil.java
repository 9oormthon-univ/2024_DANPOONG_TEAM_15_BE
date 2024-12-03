package com.ivory.ivory.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {
    private static final List<DateTimeFormatter> formatters = new ArrayList<>();

    static {
        formatters.add(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년 MM월 d일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년 M월 dd일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년MM월dd일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년M월d일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년MM월d일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy년M월dd일"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public static LocalDate parseToLocalDate(String date) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException ignored) {
                // 다음 패턴 시도
            }
        }
        throw new IllegalArgumentException("유효하지 않은 날짜 형식: " + date);
    }
}
