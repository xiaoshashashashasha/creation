package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.InteractionService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/Interaction")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;

    /**
     *发表评论
     * **/
    @PostMapping("/editorial")
    public Result editorial(@RequestBody @Validated Comment comment){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        comment.setUser_id(user_id);
        interactionService.editorial(comment);
        return Result.success();
    }

    /**
     *删除自己发布的评论
     * **/
    @DeleteMapping("/delComment")
    public Result delComment(@RequestParam Integer comment_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        interactionService.delComment(comment_id,user_id);
        return Result.success();
    }

    /**
     *分页获取图文评论
     * **/
    @GetMapping("/listComment")
    public Result<PageBean<Comment>> listComment(Integer pageNum , Integer pageSize,@RequestParam Integer creation_id){
        PageBean<Comment> pb = interactionService.listComment(pageNum,pageSize,creation_id);
        return Result.success(pb);
    }

    /**
     *点赞&取消点赞
     * **/
    @PatchMapping("/likeCreation")
    public Result likeCreation(@RequestBody @Validated Like like){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        like.setUser_id(user_id);
        like = interactionService.likeCreation(like);
        return Result.success(like.getLike_state());
    }

    /**
     *获取点赞状态
     * **/
    @GetMapping("/likeInfo")
    public Result likeInfo(@RequestParam Integer creation_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        Like like = interactionService.likeInfo(creation_id,user_id);
        return Result.success(like.getLike_state());
    }

    /**
     *收藏&取消收藏
     * **/
    public Result collect(@RequestBody @Validated Favorite favorite){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        favorite.setUser_id(user_id);
        favorite = interactionService.collectCreation(favorite);
        return Result.success(favorite.getFavorite_state());
    }

    /**
     *获取收藏状态
     * **/
    @GetMapping("/collectInfo")
    public Result collectInfo(@RequestParam Integer creation_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        Favorite favorite = interactionService.collectInfo(creation_id,user_id);
        return Result.success(favorite.getFavorite_state());
    }

    /**
     *分页查询我的收藏列表
     * **/
    public Result<PageBean<Favorite>> listFavorite(Integer pageNum , Integer pageSize){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        PageBean<Favorite> pb = interactionService.listFavorite(pageNum,pageSize,user_id);
        return Result.success(pb);
    }

}
