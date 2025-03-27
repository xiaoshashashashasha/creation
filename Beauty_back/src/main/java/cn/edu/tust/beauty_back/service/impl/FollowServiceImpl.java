package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Follow;
import cn.edu.tust.beauty_back.mapper.FollowMapper;
import cn.edu.tust.beauty_back.service.FollowService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowMapper followMapper;

    @Override
    public void add(int followed_id) {
        //获取当前用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        int follower_id = (int) map.get("user_id");

        followMapper.add(follower_id,followed_id);
    }

    @Override
    public void del(int followed_id) {
        //获取当前用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        int follower_id = (int) map.get("user_id");

        followMapper.del(follower_id,followed_id);
    }

    @Override
    public List<Integer> listFollowed() {
        //获取当前用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        int follower_id = (int) map.get("user_id");

        List<Integer> list_id = followMapper.listFollowed(follower_id);
        return list_id;
    }

    @Override
    public List<Integer> listFollower() {
        //获取当前用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        int followed_id = (int) map.get("user_id");

        List<Integer> list_id = followMapper.listFollower(followed_id);
        return list_id;
    }

    @Override
    public Integer followInfo(int followed_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int follower_id = (int) map.get("user_id");

        Follow follow = followMapper.followInfo(follower_id,followed_id);
        if (follow != null) {
            return 0;
        }

        return 1;
    }
}
