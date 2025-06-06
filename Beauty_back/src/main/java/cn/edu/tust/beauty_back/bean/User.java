package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    @NotNull//值非null
    private Integer user_id;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;

    private String user_pic;
    private int following_count; //关注数
    private int followers_count; //粉丝数

    @Min(0)
    @Max(2)
    private int role;

}
