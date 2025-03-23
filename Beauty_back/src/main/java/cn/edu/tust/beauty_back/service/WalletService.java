package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Wallet;

public interface WalletService {
    //获取钱包信息
    Wallet walletInfo(int user_id);
}
