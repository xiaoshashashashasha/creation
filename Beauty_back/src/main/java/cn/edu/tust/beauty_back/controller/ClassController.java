package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.bean.Class;
import cn.edu.tust.beauty_back.service.ClassService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;

    /**
     *新增分类
     * **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Class c) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            Class cs = classService.findByClassName(c.getClass_name());
            if(cs == null) {
                classService.add(c);
                return Result.success();
            }

            return Result.error("该分类已存在！");
        }
        return Result.error("您无权访问用户权限内容！");
    }

    /**
     *分页获取分类
     * **/
    @GetMapping("/list")
    public Result<PageBean<Class>> list(Integer pageNum, Integer pageSize) {
        PageBean<Class> pb = classService.list(pageNum, pageSize);
        return Result.success(pb);
    }

    /**
     *删除分类
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull Integer class_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if(user.getRole() == 1) {
            Class cs = classService.findClassById(class_id);
            if (cs != null) {
                classService.delClassByID(class_id);
                return Result.success();
            }
            return Result.error("该分类不存在！");
        }
        return Result.error("您无权访问用户权限内容！");

    }
}
