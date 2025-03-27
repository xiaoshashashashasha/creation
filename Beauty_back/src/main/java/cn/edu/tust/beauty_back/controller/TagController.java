package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.TagService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    /**
     *新增标签
     * **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Tag tag) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            Tag t = tagService.findByTagName(tag.getTag_name());
            if(t == null) {
                tagService.add(tag);
                return Result.success();
            }

            return Result.error("该标签已存在！");
        }
        return Result.error("您无权访问用户权限内容！");
    }

    /**
     *获取所有标签
     * **/
    @GetMapping("/list")
    public Result<PageBean<Tag>> list(Integer pageNum, Integer pageSize){
        PageBean<Tag> pb = tagService.list(pageNum, pageSize);
        return Result.success(pb);
    }


    /**
     *删除标签
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull Integer tag_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
             Tag tag = tagService.findByTagId(tag_id);
             if(tag != null) {
                 tagService.del(tag_id);
                 return Result.success();
             }
            return Result.error("该标签不存在！");
        }
        return Result.error("您无权访问用户权限内容！");
    }

}
