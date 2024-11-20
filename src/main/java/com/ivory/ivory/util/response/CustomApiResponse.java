package com.ivory.ivory.util.response;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomApiResponse<T> {

    private int status;
    private boolean success;
    private String message;
    private T data;

    //성공
    public static <T> CustomApiResponse<T> createSuccess(int status, String message,T data) {
        return CustomApiResponse.<T>builder()
                .status(status)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    //실패
    public static <T> CustomApiResponse<T> createFailWithout (int status, String message) {
        return CustomApiResponse.<T>builder()
                .status(status)
                .success(false)
                .message(message)
                .build();
    }

    //유효성 검증 실패
    public static <T> CustomApiResponse<T> createFailWithData(int status, String message, T data) {
        return CustomApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    @Builder
    public CustomApiResponse(int status, boolean success, String message, T data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }
}