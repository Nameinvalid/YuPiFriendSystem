package com.yupi.yupifriendbackgroud.constant;

import lombok.Getter;

@Getter
public enum UserConstant {

    /**
     * 在request.getSession中记录用户登录态的key
     */
    USER_LOGIN_STATE("userLoginState"),
    /**
     * 用户加密用的盐
     */
    SALT("zhangjiawei"),
    /**
     * 普通用户权限
     */
    DEFAULT_ROLE(0),
    /**
     * 管理员权限
     */
    ADMIN_ROLE(1);

    private String result;
    private int code;

    UserConstant(String result){
        this.result=result;
    }
    UserConstant(int code){
        this.code=code;
    }
}
