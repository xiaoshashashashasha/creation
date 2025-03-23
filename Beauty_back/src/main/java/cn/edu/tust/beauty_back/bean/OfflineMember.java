package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfflineMember {

    private Integer member_id;

    @NotNull
    private Integer offline_id;

    private Integer user_id;

    @NotNull
    private String user_name;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String member_name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
}
