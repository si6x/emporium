package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @author Si6x
 */
public interface UserService {

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送手机验证码
     * @param phone
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);
}
