package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Reservation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReservationMapper {

    //带参获取我的预约列表
    @Select("select * from offline_reservation where user_id = #{user_id} and status = #{status}")
    List<Reservation> myList(Integer user_id, Integer status);

    //新建预约
    @Insert("insert into offline_reservation(offline_id,member_id,user_id,start_at,end_at,created_at,updated_at)" +
            "values(#{offline_id},#{member_id},#{user_id},#{start_at},#{end_at},now(),now())")
    void add(Reservation reservation);

    //预约详情
    @Select("select * from offline_reservation where reservation_id = #{reservation_id}")
    Reservation Info(Integer reservation_id);

    //取消预约
    @Delete("delete from offline_reservation where reservation_id = #{reservation_id}")
    void del(Integer reservation_id);

    //评价
    @Update("update offline_reservation set point = #{point}, comment = #{comment}, status = #{3}, evaluate_at = now(), updated_at = now() where reservation_id = #{reservation_id}")
    void evalute(Integer reservation_id, Integer point, String comment);

    //获取评价列表
    @Select("select reservation_id, user_id, point, comment, evaluate_at from offline_reservation where offline_id = #{offline_id}")
    List<Reservation> commentList(Integer offline_id);

    //带参获取门店下预约
    @Select("select * from offline_reservation where offline_id = #{offline_id} and status = #{status}")
    List<Reservation> offlineList(Integer offline_id, Integer status);

    //核销预约
    @Update("update offline_reservation set reservation_id = #{reservation_id}, status = #{status}, updated_at = now()")
    void updateStatus(Integer reservation_id, Integer status);
}
