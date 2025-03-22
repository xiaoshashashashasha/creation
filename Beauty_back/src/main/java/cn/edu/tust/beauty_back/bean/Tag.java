package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Tag {
    private int tag_id;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,8}$")
    private String tag_name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
}
