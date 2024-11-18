package com.ivory.ivory.util.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
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
        return new CustomApiResponse<T>(status, true, message, data);
    }

    //실패
    public static <T> CustomApiResponse<T> createFailWithout (int status, String message) {
        return new CustomApiResponse<T>(status, false,  message,null);
    }
}