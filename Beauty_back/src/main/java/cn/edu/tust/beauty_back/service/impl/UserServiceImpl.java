package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.mapper.UserMapper;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void register(String username, String password, String gender, String email) {
        //先加密
        String jwt_password = Md5Util.getMd5String(password);
        //添加用户
        userMapper.add(username,jwt_password,gender,email);
    }
}
