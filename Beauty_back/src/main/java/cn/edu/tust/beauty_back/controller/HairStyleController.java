package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.HairStyle;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.bean.User;
import cn.edu.tust.beauty_back.service.HairStyleService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/hairstyle")
public class HairStyleController {

    @Autowired
    private HairStyleService hairStyleService;
    @Autowired
    private UserService userService;

    /**
     *新增发型
     * **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated HairStyle hairStyle) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            hairStyleService.add(hairStyle);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");
    }

    /**
     *分页查询发型信息
     * **/
    @GetMapping("/list")
    public Result<PageBean<HairStyle>> list(Integer pageNum, Integer pageSize, @RequestParam(required = false) String keyWord) {
        PageBean<HairStyle> pb = hairStyleService.list(pageNum,pageSize,keyWord);
        return Result.success(pb);
    }

    /**
     *获取发型信息
     * **/
    @GetMapping("/hairStyleInfo")
    public Result hairStyleInfo(@RequestParam @NotNull Integer hairstyle_id) {
        HairStyle hairStyle = hairStyleService.getHairStyleById(hairstyle_id);
        return Result.success(hairStyle);
    }

    /**
     *编辑发型信息
     * **/
    @PutMapping("/update")
    public Result update(@RequestBody @Validated HairStyle hairStyle) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            hairStyleService.update(hairStyle);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");

    }

    /**
     *上传发型封面
     * **/
    @PatchMapping("/updateCover")
    public Result updateCover(@RequestParam @URL String coverUrl, @RequestParam @NotNull Integer hairstyle_id){
        hairStyleService.updateCover(coverUrl, hairstyle_id);
        return Result.success();
    }

    /**
     *删除发型信息
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull Integer hairstyle_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            hairStyleService.del(hairstyle_id);
            return Result.success();
        }
        return Result.error("您无权访问用户权限内容！");

    }

}
