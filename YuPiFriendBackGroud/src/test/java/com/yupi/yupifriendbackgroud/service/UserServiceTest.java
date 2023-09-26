package com.yupi.yupifriendbackgroud.service;


import com.yupi.yupifriendbackgroud.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author 张家维
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user=new User();
        user.setUsername("zhangjiawei");
        user.setUserAccount("123456");
        user.setAvatarUrl("C:\\Users\\lenovo\\Desktop\\1017213133ml.png");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("15030912132");
        user.setEmail("zjw0145@qq.com");
        user.setTags("优秀");


        boolean result=userService.save(user);

        System.out.println(user.getId());



    }

    @Test
    void userRegister() {
        String userAccount="zhang";
        String userPassword="12345678910";
        String checkPassword="12345678910";
        long result=userService.userRegister(userAccount,userPassword,checkPassword);
        System.out.println(result);
        Assertions.assertTrue(result>0);




    }
}