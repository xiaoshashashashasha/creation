package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.JwtUtil;
import cn.edu.tust.beauty_back.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{3,12}$") String username, @Pattern(regexp = "^\\S{9,16}$") String password, @Pattern(regexp = "^.+$") String gender, @Pattern(regexp = "^.+$") String email) {

        //查询用户是否存在
        User user = userService.findByUserName(username);
        if (user == null) {
            userService.register(username, password, gender, email);
            return Result.success();
        } else {
            return Result.error("用户名已被占用！");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{3,12}$") String username, @Pattern(regexp = "^\\S{9,16}$") String password) {
        //查询用户是否存在
        User user = userService.findByUserName(username);
        if (user == null) {
            return Result.error("用户名错误");
        }
        //校验密码
        if (Md5Util.verify(password, user.getJwt_password())) {
            //生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getUser_id());
            claims.put("username", user.getUsername());
            String Token = JwtUtil.genToken(claims);
            return Result.success(Token);

        }

        return Result.error("密码错误");
    }
}
