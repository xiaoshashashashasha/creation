package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.User;

import java.util.List;

public interface FollowService {
    //新增关注关系
    void add(int followed_id);
    //删除关注关系
    void del(int followed_id);
    //获取关注者列表
    List<Integer> listFollowed();
    //获取粉丝列表
    List<Integer> listFollower();
    //获取关注状态
    Integer followInfo(int followed_id);
}
