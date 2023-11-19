package com.yupi.yupifriendbackgroud.model.domain.request;

import lombok.Data;


import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * 最好加一个序列化，
 * @author 张家维
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 5698003350203508304L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String idNumber;

}
