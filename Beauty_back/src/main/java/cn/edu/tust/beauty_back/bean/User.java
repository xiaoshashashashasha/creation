package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    @NotNull//值非null
    private int user_id;

    @NotEmpty//值非null且非空
    @Pattern(regexp = "^\\S{3,12}$")
    private String username;

    @JsonIgnore //使SpringMVC在转化当前对象为Json字符串时，忽略该属性，以去除获得Json字符串中的对应项
    private String jwt_password;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String email;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int following_count; //关注数
    private int followers_count; //粉丝数
    private int role;

}
