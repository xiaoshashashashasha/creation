package cn.edu.tust.beauty_back.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Wallet {
    private Integer wallet_id;
    private Integer user_id;
    private int coins;
    private int membership;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
