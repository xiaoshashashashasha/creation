package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.ChatListItem;
import cn.edu.tust.beauty_back.bean.PrMessage;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.mapper.PrMessageMapper;
import cn.edu.tust.beauty_back.service.PrMessageService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import cn.edu.tust.beauty_back.websocket.PrivateChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PrMessageServiceImpl implements PrMessageService {

    @Autowired
    PrMessageMapper prMessageMapper;

    @Override
    public Result sendMessage(Map<String, Object> params) {
        if (params == null || !params.containsKey("to_id") || !params.containsKey("content")) {
            return Result.error("发送消息参数缺失");
        }

        Integer toId = Integer.valueOf(params.get("to_id").toString());
        String content = params.get("content").toString();

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer fromId = (Integer) map.get("user_id");

        PrMessage message = new PrMessage();
        message.setFrom_id(fromId);
        message.setTo_id(toId);
        message.setContent(content);
        message.setCreated_at(LocalDateTime.now());

        prMessageMapper.sendMessage(message);

        // 使用 WebSocket 推送消息
        PrivateChatEndpoint.sendMessage(toId, message);

        return Result.success();
    }



    @Override
    public Result<List<PrMessage>> getHistory(Integer pageNum, Integer pageSize, Integer target_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        int offset = (pageNum - 1) * pageSize;

        List<PrMessage> list = prMessageMapper.getHistory(user_id, target_id, offset, pageSize);

        return Result.success(list);
    }



    @Override
    public Result<Integer> getUnReadCount() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        Integer count = prMessageMapper.getUnReadCount(user_id);
        return Result.success(count);
    }

    @Override
    public Result setMessageRead(Integer target_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        prMessageMapper.setMessageRead(user_id,target_id);

        return Result.success();
    }

    @Override
    public Result<List<ChatListItem>> getChatList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        List<ChatListItem> list = prMessageMapper.getChatList(user_id);
        return Result.success(list);
    }
}
