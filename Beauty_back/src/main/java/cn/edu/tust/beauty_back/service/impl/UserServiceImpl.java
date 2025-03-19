package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.mapper.UserMapper;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.Md5Util;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public User findByUserId(Integer user_id) {
        User user = userMapper.findByUserId(user_id);
        return user;
    }

    @Override
    public void register(String username, String password,String nickname, String gender, String email) {
        //先加密
        String jwt_password = Md5Util.getMd5String(password);
        //添加用户
        userMapper.add(username,jwt_password,nickname,gender,email);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        //从线程中获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        userMapper.updateAvatar(user_id,avatarUrl);
    }

    @Override
    public void updatePwd(String newPwd) {
        //从线程中获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        userMapper.updatePwd(user_id,Md5Util.getMd5String(newPwd));
    }

    @Override
    public void updateRole(int user_id, int role) {
        userMapper.updateRole(user_id,role);
    }


}
