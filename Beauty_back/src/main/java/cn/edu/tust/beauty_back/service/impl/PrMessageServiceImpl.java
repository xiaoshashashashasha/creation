package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.ChatListItem;
import cn.edu.tust.beauty_back.bean.PrMessage;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.mapper.PrMessageMapper;
import cn.edu.tust.beauty_back.service.PrMessageService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class PrMessageServiceImpl implements PrMessageService {

    @Autowired
    PrMessageMapper prMessageMapper;

    @Override
    public Result sendMessage(Map<String, Object> params) {
        if (params == null) {
            return Result.error("参数错误");
        }

        Object toIdObj = params.get("to_id");
        Object contentObj = params.get("content");

        if (toIdObj == null || contentObj == null) {
            return Result.error("参数缺失");
        }

        Integer to_id = Integer.parseInt(toIdObj.toString());
        String content = contentObj.toString();

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer from_id = (Integer) map.get("user_id");

        PrMessage msg = new PrMessage();
        msg.setContent(content);
        msg.setFrom_id(from_id);
        msg.setTo_id(to_id);

        prMessageMapper.sendMessage(msg);

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
