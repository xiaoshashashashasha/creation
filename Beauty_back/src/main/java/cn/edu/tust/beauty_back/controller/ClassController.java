package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Class;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.service.ClassService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     *新增分类
     * **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Class c) {
        classService.add(c);
        return Result.success();
    }

    /**
     *获取分类
     * **/
    @GetMapping("/list")
    public Result<List<Class>> List() {
        List<Class> cs = classService.list();
        return Result.success(cs);
    }

    /**
     *删除分类
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull int class_id) {
        Class cs = classService.findClassById(class_id);

        if (cs != null) {
            classService.delClassByID(class_id);
            return Result.success();
        }

        return Result.error("该分类不存在！");
    }
}
