package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Wallet;
import cn.edu.tust.beauty_back.mapper.WalletMapper;
import cn.edu.tust.beauty_back.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletMapper walletMapper;

    @Override
    public Wallet walletInfo(int user_id) {
        return walletMapper.findWalletByUId(user_id);
    }
}
