package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.ChatListItem;
import cn.edu.tust.beauty_back.bean.CreationShare;
import cn.edu.tust.beauty_back.bean.HairstyleShare;
import cn.edu.tust.beauty_back.bean.PrMessage;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PrMessageMapper {
    //发送消息
    @Insert("insert into beauty_prmessage(from_id, to_id, content, created_at, type, content_id)" +
            "values(#{from_id}, #{to_id}, #{content}, #{created_at}, #{type}, #{content_id} )")
    void sendMessage(PrMessage msg);

    //发送消息
    @Insert("insert into beauty_prmessage(from_id, to_id, content, created_at, type, content_id, cover_pic, title)" +
            "values(#{from_id}, #{to_id}, #{content}, #{created_at}, #{type}, #{content_id}, #{cover_pic}, #{title} )")
    void sendMessageB(PrMessage msg);

    //获取历史消息
    @Select("""
    SELECT * FROM beauty_prmessage
    WHERE (from_id = #{user_id} AND to_id = #{target_id})
       OR (from_id = #{target_id} AND to_id = #{user_id})
    ORDER BY created_at DESC
    LIMIT #{offset}, #{pageSize}
    """)
    List<PrMessage> getHistory(Integer user_id, Integer target_id, Integer offset, Integer pageSize);

    //获取总未读消息数
    @Select("select count(*) from beauty_prmessage where to_id = #{user_id} and is_read = 0")
    Integer getUnReadCount(Integer user_id);

    //设置所有消息已读
    @Update("update beauty_prmessage set is_read = 1 where from_id = #{target_id} and to_id = #{user_id} and is_read = 0")
    void setMessageRead(Integer user_id, Integer target_id);

    // 查询聊天列表
    @Select("""
        SELECT 
            t.target_id,
            t.lastTime,
            p2.content AS lastMessage,
            t.unReadCount
        FROM (
            SELECT 
                CASE 
                    WHEN from_id = #{user_id} THEN to_id
                    ELSE from_id
                END AS target_id,
                MAX(created_at) AS lastTime,
                SUM(CASE 
                        WHEN to_id = #{user_id} AND is_read = 0 THEN 1 
                        ELSE 0 
                    END) AS unReadCount
            FROM beauty_prmessage
            WHERE from_id = #{user_id} OR to_id = #{user_id}
            GROUP BY target_id
        ) t
        LEFT JOIN beauty_prmessage p2
        ON (
            (p2.from_id = #{user_id} AND p2.to_id = t.target_id)
            OR (p2.from_id = t.target_id AND p2.to_id = #{user_id})
        )
        AND p2.created_at = t.lastTime
        ORDER BY t.lastTime DESC
    """)
    List<ChatListItem> getChatList(Integer user_id);

    @Select("select cover_pic,title from beauty_creation where creation_id = #{contentId}")
    CreationShare getCInfo(Integer contentId);

    @Select("select hairstyle_name,hairstyle_pic from beauty_hairstyle where hairstyle_id = #{contentId}")
    HairstyleShare getHInfo(Integer contentId);
}
