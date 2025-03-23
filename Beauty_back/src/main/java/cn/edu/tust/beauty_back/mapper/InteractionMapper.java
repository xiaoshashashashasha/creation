package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Comment;
import cn.edu.tust.beauty_back.bean.Favorite;
import cn.edu.tust.beauty_back.bean.Like;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InteractionMapper {

    //新增评论
    @Insert("insert into beauty_comments(creation_id,user_id,content,created_at)" +
            "values(#{creation_id},#{user_id},#{content},now())")
    void addComment(Comment comment);

    //删除自己发布的评论
    @Delete("delete from beauty_comments where comment_id = #{comment_id} and user_id = #{user_id}")
    void delComment(Integer comment_id, Integer user_id);

    //分页获取图文评论
    @Select("select * from beauty_comments where creation_id = #{creation_id}")
    List<Comment> listComment(Integer creation_id);

    //新增like
    @Insert("insert into beauty_likes(creation_id, user_id, updated_at)" +
            "values(#{creation_id},#{user_id},now())")
    void addLike(Like like);

    //点赞&取消点赞
    @Update("update beauty_likes set like_state = #{like_state},updated_at = now() where like_id = #{like_id}")
    void updateLike(Like like);

    //获取点赞状态
    @Select("select * from beauty_likes where creation_id = #{creation_id} and user_id = #{user_id}")
    Like likeInfo(Integer creation_id, Integer user_id);

    //新增收藏
    @Insert("insert into beauty_favorites(creation_id, user_id, updated_at)" +
            "values(#{creation_id},#{user_id},now())")
    void addFavorite(Favorite favorite);

    //收藏&取消收藏
    @Update("update beauty_favorites set favorite_state = #{favorite_state},updated_at = now() where favorite_id = #{favorite_id}")
    void updateFavorite(Favorite favorite);

    //获取收藏状态
    @Select("select * from beauty_favorites where creation_id = #{creation_id} and user_id = #{user_id}")
    Favorite favoriteInfo(Integer creation_id, Integer user_id);

    //分页获取我的收藏列表
    @Select("select * from beauty_favorites where user_id = #{user_id} and favorite_state = 0")
    List<Favorite> listFavorite(Integer user_id);
}
