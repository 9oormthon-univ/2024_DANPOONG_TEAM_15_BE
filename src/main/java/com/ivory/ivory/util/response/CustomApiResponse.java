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
    //status, message, data
    private int status;

    private String message;

    private T data;


    //성공
    //static : CustomApiResponse 클래스를 new로 선언하지 않아도 사용할 수 있음
    public static <T> CustomApiResponse<T> createSuccess(int status,String message, T data){
        return new CustomApiResponse<>(status,message,data);
    }

    //실패
    public static <T> CustomApiResponse<T> createFailWithout(int status, String message){
        return new CustomApiResponse<>(status, message,null);
    }
}
