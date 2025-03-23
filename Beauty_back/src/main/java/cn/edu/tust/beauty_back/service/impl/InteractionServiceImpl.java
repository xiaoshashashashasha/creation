package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.mapper.CreationMapper;
import cn.edu.tust.beauty_back.mapper.InteractionMapper;
import cn.edu.tust.beauty_back.service.InteractionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionServiceImpl implements InteractionService {
    @Autowired
    InteractionMapper interactionMapper;

    @Override
    public void editorial(Comment comment) {
        interactionMapper.addComment(comment);
    }

    @Override
    public void delComment(Integer comment_id, Integer user_id) {
        interactionMapper.delComment(comment_id,user_id);
    }

    @Override
    public PageBean<Comment> listComment(Integer pageNum, Integer pageSize, Integer creation_id) {
        PageBean<Comment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Comment> list = interactionMapper.listComment(creation_id);

        Page<Comment> p =(Page<Comment>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public Like likeCreation(Like like) {
        if (like.getLike_state() == null){
            interactionMapper.addLike(like);
            like.setLike_state(0);
        }else if (like.getLike_state() == 0){
            like.setLike_state(1);
            interactionMapper.updateLike(like);
        }else {
            like.setLike_state(0);
            interactionMapper.updateLike(like);
        }
        return like;
    }

    @Override
    public Like likeInfo(Integer creation_id, Integer user_id) {
        Like like = interactionMapper.likeInfo(creation_id, user_id);
        return like;
    }

    @Override
    public Favorite collectCreation(Favorite favorite) {
        if (favorite.getFavorite_state() == null){
            interactionMapper.addFavorite(favorite);
            favorite.setFavorite_state(0);
        }else if (favorite.getFavorite_state() == 0){
            favorite.setFavorite_state(1);
            interactionMapper.updateFavorite(favorite);
        }else {
            favorite.setFavorite_state(0);
            interactionMapper.updateFavorite(favorite);
        }
        return favorite;
    }

    @Override
    public Favorite collectInfo(Integer creation_id, Integer user_id) {
        Favorite favorite = interactionMapper.favoriteInfo(creation_id,user_id);
        return favorite;
    }

    @Override
    public PageBean<Favorite> listFavorite(Integer pageNum, Integer pageSize, Integer user_id) {

        PageBean<Favorite> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Favorite> list = interactionMapper.listFavorite(user_id);

        Page<Favorite> p = (Page<Favorite>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }
}
