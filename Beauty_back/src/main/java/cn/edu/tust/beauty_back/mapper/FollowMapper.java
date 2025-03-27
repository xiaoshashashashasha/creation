package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Follow;
import cn.edu.tust.beauty_back.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper {

    //新增关注关系
    @Insert("insert into beauty_follow(follower_id,followed_id,created_at)" +
            "values(#{follower_id},#{followed_id},now())")
    void add(int follower_id, int followed_id);

    //删除关注关系
    @Delete("delete from beauty_follow where follower_id = #{follower_id} and followed_id = #{followed_id}")
    void del(int follower_id, int followed_id);

    //获取关注id列表
    @Select("select followed_id from beauty_follow where follower_id = #{follower_id}")
    List<Integer> listFollowed(int follower_id);

    //获取粉丝id列表
    @Select("select follower_id from beauty_follow where followed_id = #{followed_id}")
    List<Integer> listFollower(int followed_id);

    //获取关注状态
    @Select("select * from beauty_follow where follower_id = #{follower_id} and followed_id = #{followed_id}")
    Follow followInfo(int follower_id, int followed_id);
}
