package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Creation {
    private Integer creation_id;
    private Integer user_id;

    @Pattern(regexp = "^\\S{1,10}$")
    private String title;

    @Pattern(regexp = "^\\S{1,30}$")
    private String abs_text;

    @URL
    private String cover_pic;

    @NotEmpty
    private String content;

    @NotNull
    private Integer class_id;

    private int views;

    private int likes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;

    //审核意见：0-通过，1-撤回，2-未审核
    private int examine;

    //撤回理由
    private String review_comments;
}
