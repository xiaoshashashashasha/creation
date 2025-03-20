package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.User;

public interface UserService {
    //根据用户名查询
    User findByUserName(String username);
    //根据用户id查询
    User findByUserId(Integer user_id);
    //注册新用户
    void register(String username, String password,String nickname, String gender, String email);
    //更新用户信息
    void update(User user);
    //更新头像
    void updateAvatar(String avatarUrl);
    //更新密码
    void updatePwd(String newPwd);
    //更新权限
    void updateRole(int user_id, int role);
    //条件分页查询用户
    PageBean<User> list(Integer pageNum, Integer pageSize, Integer user_id, String keyWord);
}
