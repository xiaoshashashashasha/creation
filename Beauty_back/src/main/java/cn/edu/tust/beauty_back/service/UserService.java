package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.User;

public interface UserService {
    //根据用户名查询
    User findByUserName(String username);
    //注册新用户
    void register(String username, String password,String nickname, String gender, String email);
    //更新用户信息
    void update(User user);
    //更新头像
    void updateAvatar(String avatarUrl);
    //更新密码
    void updatePwd(String newPwd);
}
