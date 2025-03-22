package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.TagService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
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
            tagService.add(tag);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");
    }

    /**
     *获取所有标签
     * **/
    @GetMapping("/list")
    public Result<List<Tag>> list(){
        List<Tag> list = tagService.list();
        return Result.success(list);
    }

    /**
     *删除标签
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam Integer tag_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            tagService.del(tag_id);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");
    }

}
