package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.HairStyle;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.mapper.UserMapper;
import cn.edu.tust.beauty_back.mapper.WalletMapper;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.Md5Util;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletMapper walletMapper;

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
        //获取用户id创建钱包
        User user = userMapper.findByUserName(username);
        walletMapper.add(user.getUser_id());
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

    @Override
    public PageBean<User> list(Integer pageNum, Integer pageSize, Integer user_id, String keyWord) {
        //创建PageBean对象
        PageBean<User> pb = new PageBean<>();

        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        //调用Mapper
        List<User> us = userMapper.list(user_id,keyWord);

        //Page中提供了可获取PageHelper分页查询后，得到的总记录条数和当前页数据
        Page<User> p = (Page<User>) us;

        //将数据填充到PageBean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }


}
