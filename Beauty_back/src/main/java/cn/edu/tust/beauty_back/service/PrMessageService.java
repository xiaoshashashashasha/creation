package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.ChatListItem;
import cn.edu.tust.beauty_back.bean.PrMessage;
import cn.edu.tust.beauty_back.bean.Result;

import java.util.List;
import java.util.Map;

public interface PrMessageService {
    Result sendMessage(Map<String, Object> params);

    Result<List<PrMessage>> getHistory(Integer pageNum, Integer pageSize, Integer target_id);

    Result<Integer> getUnReadCount();

    Result setMessageRead(Integer target_id);

    Result<List<ChatListItem>> getChatList();
}
