package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer comment_id;

    @NotNull
    private Integer creation_id;

    private Integer user_id;

    private String nickname;

    @NotEmpty
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
}
