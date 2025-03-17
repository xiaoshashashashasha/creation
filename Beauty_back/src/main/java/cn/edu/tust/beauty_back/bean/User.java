package cn.edu.tust.beauty_back.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private int user_id;
    private String username;
    private String jwt_password;
    private String gender;
    private String email;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private int following_count; //关注数
    private int followers_count; //粉丝数
    private int role;

}
