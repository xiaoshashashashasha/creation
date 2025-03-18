package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.JwtUtil;
import cn.edu.tust.beauty_back.utils.Md5Util;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/userInfo")
    public Result userInfo() {

        //从线程中获取经过解析的用户数据
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

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
}
