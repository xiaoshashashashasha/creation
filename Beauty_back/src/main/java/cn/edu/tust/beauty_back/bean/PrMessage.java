package cn.edu.tust.beauty_back.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class PrMessage {
    private Integer message_id;
    private Integer from_id;
    private Integer to_id;
    private String content;

    @URL
    private String cover_pic;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
    private Integer is_read;
    private Integer type = 0;
    private Integer content_id = 0;
}
