package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.bean.Wallet;
import cn.edu.tust.beauty_back.service.WalletService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/walletInfo")
    public Result walletInfo(@RequestParam int user_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer login_id = (Integer) map.get("user_id");
        if(login_id == user_id){
            Wallet wallet = walletService.walletInfo(user_id);
            return Result.success(wallet);
        }
        return Result.error("无权访问他人钱包！");
    }
}
