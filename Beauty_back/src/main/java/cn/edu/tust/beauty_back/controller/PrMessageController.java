package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.ChatListItem;
import cn.edu.tust.beauty_back.bean.PrMessage;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.service.PrMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/prMessage")
public class PrMessageController {

    @Autowired
    private PrMessageService prMessageService;

    /**
     *发送消息
     * **/
    @PostMapping("/send")
    public Result sendMessage(@RequestBody Map<String, Object> params) {
        return prMessageService.sendMessage(params);
    }

    /**
     *获取历史消息记录
     * **/
    @GetMapping("/history")
    public Result<List<PrMessage>> getHistory(@RequestParam Integer pageNum, Integer pageSize , Integer target_id) {
        return prMessageService.getHistory(pageNum,pageSize,target_id);
    }

    /**
     *获取总未读消息数
     * **/
    @GetMapping("/tUnReadCount")
    public Result<Integer> getUnReadCount(){
        return prMessageService.getUnReadCount();
    }

    /**
     *更新已读
     * **/
    @PostMapping("/setAllRead")
    public Result setMessageRead(Integer target_id) {
        return prMessageService.setMessageRead(target_id);
    }

    /**
     *获取聊天列表
     * **/
    @GetMapping("/getChatList")
    public Result<List<ChatListItem>> getChatList(){
        return prMessageService.getChatList();
    }
}
