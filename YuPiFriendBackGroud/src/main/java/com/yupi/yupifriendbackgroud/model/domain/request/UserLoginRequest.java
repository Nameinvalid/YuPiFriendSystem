package com.yupi.yupifriendbackgroud.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * 最好加一个序列化，前后盾交互的时候不容易丢失信息
 * @author 张家维
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 5698003350203508304L;
    private String userAccount;
    private String userPassword;

}
