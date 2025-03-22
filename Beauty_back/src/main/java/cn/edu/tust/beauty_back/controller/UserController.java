package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.service.FollowService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.JwtUtil;
import cn.edu.tust.beauty_back.utils.Md5Util;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;

    /**
     *注册
     * **/
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{3,12}$") String username, @Pattern(regexp = "^\\S{9,16}$") String password, @Pattern(regexp = "^\\S{1,10}$") String nickname, @Pattern(regexp = "^.+$") String gender, @Pattern(regexp = "^.+$") String email) {

        //根据用户名查询用户是否存在
        User user = userService.findByUserName(username);
        if (user == null) {
            userService.register(username, password, nickname, gender, email);
            return Result.success();
        } else {
            return Result.error("用户名已被占用！");
        }
    }

    /**
     *登录
     * **/
    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{3,12}$") String username, @Pattern(regexp = "^\\S{9,16}$") String password) {
        //根据用户名查询用户是否存在
        User user = userService.findByUserName(username);
        if (user == null) {
            return Result.error("用户名错误");
        }
        //校验密码
        if (Md5Util.verify(password, user.getJwt_password())) {
            //生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_id", user.getUser_id());
            claims.put("username", user.getUsername());
            String Token = JwtUtil.genToken(claims);
            return Result.success(Token);

        }

        return Result.error("密码错误");
    }

    /**
     *获取用户详细信息
     * **/
    @GetMapping("/userInfo")
    public Result userInfo() {

        //从线程中获取经过解析的用户数据
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    /**
     *更新基本信息
     * **/
    @PatchMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    /**
     *上传头像
     * **/
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    /**
     *更新密码
     * **/
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        //校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数！");
        }

        //对比密码
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        if(!rePwd.equals(newPwd)){
            return Result.error("两次密码不一致！");
        }
        if(!Md5Util.verify(oldPwd, user.getJwt_password())) {
            return Result.error("原密码错误！");
        }

        //更新密码
        userService.updatePwd(newPwd);
        return Result.success();

    }

    /**
     *关注他人
     * **/
    @PostMapping("/follow")
    public Result follow(int followed_id){
        followService.add(followed_id);
        return Result.success();
    }

    /**
     *取消关注
     * **/
    @DeleteMapping("/cancelFollow")
    public Result cancelFollow(@RequestParam int followed_id){
        followService.del(followed_id);
        return Result.success();
    }

    /**
     *查询关注用户列表
     * **/
    @GetMapping("/listFollowed")
    public Result<List<User>> listFollowed(){
        List<Integer> list_id = followService.listFollowed();
        List<User> list_user = new ArrayList<>();
        for (Integer user_id : list_id) {
            list_user.add(userService.findByUserId(user_id));
        }
        return Result.success(list_user);
    }

    /**
     *查询粉丝列表
     * **/
    @GetMapping("/listFollower")
    public Result<List<User>> listFollower(){
        List<Integer> list_id = followService.listFollower();
        List<User> list_user = new ArrayList<>();
        for (Integer user_id : list_id) {
            list_user.add(userService.findByUserId(user_id));
        }
        return Result.success(list_user);
    }

    /**
     *更改用户权限
     * **/
    @PatchMapping("/changeRole")
    public Result changeRole(@NotNull int user_id, @Min(0) @Max(2) int role) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            userService.updateRole(user_id,role);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");
    }

    /**
     *管理者分页查询用户
     * **/
    @GetMapping("/list")
    public Result<PageBean<User>> list(Integer pageNum, Integer pageSize,@RequestParam(required = false) Integer user_id,@RequestParam(required = false) String keyWord) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            if (user_id != null && keyWord != null ){
                return Result.error("非法参数！");
            }
            PageBean<User> pb = userService.list(pageNum,pageSize,user_id,keyWord);
            return Result.success(pb);
        }
        return Result.error("您无权访问用户权限内容！");
    }
}
