package cn.edu.tust.beauty_back.service;


import cn.edu.tust.beauty_back.bean.Comment;
import cn.edu.tust.beauty_back.bean.Favorite;
import cn.edu.tust.beauty_back.bean.Like;
import cn.edu.tust.beauty_back.bean.PageBean;

public interface InteractionService {
    //发表评论
    void editorial(Comment comment);
    //删除自己发布的评论
    void delComment(Integer comment_id, Integer user_id);
    //分页获取图文评论
    PageBean<Comment> listComment(Integer pageNum, Integer pageSize, Integer creation_id);
    //点赞&取消点赞
    Like likeCreation(Like like);
    //获取点赞状态
    Like likeInfo(Integer creation_id, Integer user_id);
    //收藏&取消收藏
    Favorite collectCreation(Favorite favorite);
    //获取收藏状态
    Favorite collectInfo(Integer creation_id, Integer user_id);
    //分页获取我的收藏列表
    PageBean<Favorite> listFavorite(Integer pageNum, Integer pageSize, Integer user_id);
}
