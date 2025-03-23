package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Favorite {
    private Integer favorite_id;

    @NotNull
    private Integer creation_id;

    @NotNull
    private Integer user_id;

    private Integer favorite_state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
}
