package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Offline;
import cn.edu.tust.beauty_back.bean.OfflineMember;
import cn.edu.tust.beauty_back.bean.OfflineRequest;
import cn.edu.tust.beauty_back.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OfflineMapper {
    //新增线下门店申请
    @Insert("insert into offline_request(manager_id,target_city,created_at,updated_at)" +
            "values(#{manager_id},#{target_city},now(),now())")
    void addRequest(OfflineRequest offlineRequest);

    //查看我的门店申请列表
    @Select("select * from offline_request where manager_id = #{manager_id}")
    List<OfflineRequest> requestList(Integer manager_id);

    //查看申请详情
    @Select("select * from offline_request where request_id = #{request_id}")
    OfflineRequest requestInfo(Integer request_id);

    //获取门店列表
    List<Offline> offlineList(String offline_city);

    //查看我的门店列表
    @Select("select * from beauty_offline where manager_id = #{manager_id}")
    List<Offline> myOfflineList(Integer manager_id);

    //查看门店详情
    @Select("select * from beauty_offline where offline_id = #{offline_id}")
    Offline offlineInfo(Integer offline_id);

    //完善门店信息
    @Update("update beauty_offline set offline_name = #{offline_name}, offline_position = #{offline_position},offline_pic = #{offline_pic}, offline_content=#{offline_content}, offline_phone = #{offline_phone}, updated_at = now() where offline_id =  #{offline_id}")
    void improveOffline(Offline offline);

    //置入门店成员
    @Insert("insert into offline_member(offline_id,user_id,member_name,created_at)" +
            "values(#{offline_id},#{user_id},#{member_name},now())")
    void addMember(OfflineMember offlineMember);

    //获取成员列表
    @Select("select * from offline_member where offline_id = #{offline_id}")
    List<OfflineMember> memberList(Integer offline_id);

    //删除门店成员
    @Delete("delete from offline_member where member_id = #{member_id}")
    void delMember(Integer member_id);

    //获取门店申请列表
    @Select("select * from offline_request where examine = 2")
    List<OfflineRequest> requestListExa();

    //审核门店申请
    @Update("update offline_request set examine = #{examine} , review_comments = #{review_comments} where request_id = #{request_id}")
    void updateExamine(Integer request_id, Integer examine, String review_comments);

    //创建门店框架
    @Insert("insert into beauty_offline(manager_id,offline_city , created_at, updated_at)" +
            "values(#{manager_id},#{target_city} ,now(),now())")
    void addOffline(Integer manager_id, String target_city);

    //删除门店
    @Delete("delete from beauty_offline where offline_id = #{offline_id}")
    void delOffline(Integer offline_id);

    //根据用户id和门店id查找对应成员是否存在
    @Select("select user_id from offline_member where offline_id = #{offline_id} and user_id = #{user_id}")
    User findMemberByUId(Integer offline_id, Integer user_id);

    //根据成员id寻找对应成员
    @Select("select * from offline_member where member_id = #{member_id}")
    OfflineMember findMemberByMId(Integer member_id);
}
