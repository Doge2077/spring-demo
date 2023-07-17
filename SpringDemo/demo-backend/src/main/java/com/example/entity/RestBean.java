package com.example.entity;

import lombok.Data;

@Data
public class RestBean<T> {

    private Integer status;    // 状态码
    private Boolean success;   // 登录是否成功
    private T message;         // 额外信息

    private RestBean(Integer status, Boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, true, null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, true, data);
    }

    public static <T> RestBean<T> failure(Integer status) {
        return new RestBean<>(status, false, null);
    }

    public static <T> RestBean<T> failure(Integer status, T data) {
        return new RestBean<>(status, false, data);
    }

}
