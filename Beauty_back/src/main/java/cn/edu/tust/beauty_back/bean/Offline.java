package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Offline {
    @NotNull
    private Integer offline_id;

    @NotEmpty
    @Pattern(regexp = "^\\S{3,12}$")
    private String offline_name;

    @URL
    private String offline_pic;

    private String offline_content;

    @NotEmpty
    @Pattern(regexp = "^\\S{3,99}$")
    private String offline_position;

    @NotEmpty
    @Pattern(regexp = "^\\S{6,18}$")
    private String offline_phone;

    private Integer manager_id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;
}
