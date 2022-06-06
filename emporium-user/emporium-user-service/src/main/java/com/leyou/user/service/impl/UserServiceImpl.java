package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author Si6x
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:";
    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 发送手机验证码
     * @param phone
     */
    @Override
    public Boolean sendVerifyCode(String phone) {
        //生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            //发送消息到rabbitmq
            Map<String,String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","sms.verify.code",msg);
            //把验证码保存到redis中
            this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return true;
        } catch (AmqpException e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @Override
    public Boolean register(User user, String code) {
        //查询redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        //校验验证码
        if (!StringUtils.equals(code,redisCode)){
            return false;
        }

        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //对密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());

        //添加到数据库
        return this.userMapper.insertSelective(user) == 1;
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        //查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);

        //判断user是否为空
        if (user == null){
            return null;
        }

        //获取盐，对用户输入的密码加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());

        //和数据库中的密码比较如果不一致返回null
        if (!StringUtils.equals(password,user.getPassword())){
            return null;
        }

        //用户名和密码都正确
        return user;
    }


}
