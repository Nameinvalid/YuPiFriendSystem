package com.yupi.yupifriendbackgroud.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态：0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     * TableLogic主要是为了告诉mybatisplus框架这个字段是逻辑删除字段
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 
     */
    private Integer userRole;

    /**
     * 标签列表
     */
    private String tags;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}