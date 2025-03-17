package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password, String gender, String email);
}
