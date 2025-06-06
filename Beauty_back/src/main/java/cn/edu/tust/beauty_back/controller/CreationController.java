package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.CreationService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/creation")
public class CreationController {

    @Autowired
    private CreationService creationService;
    @Autowired
    private UserService userService;

    /**
     * 发布图文内容,将examine设置为2
     **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Creation creation) {
        creation.setExamine(2);
        creationService.add(creation);
        return Result.success();
    }

    /**
     * 编辑图文内容,将examine重置为2
     **/
    @PutMapping("/update")
    public Result update(@RequestBody @Validated Creation creation) {
        creation.setExamine(2);
        creationService.update(creation);
        return Result.success();
    }

    /**
     * 获取详细内容
     **/
    @GetMapping("/creationInfo")
    public Result<Creation> creationInfo(@RequestParam @NotNull Integer creation_id) {
        Creation creation = creationService.getCreationByCId(creation_id);
        return Result.success(creation);
    }

    /**
     * 分页获取我发布的图文列表
     **/
    @GetMapping("/myList")
    public Result<PageBean<Creation>> myList(Integer pageNum, Integer pageSize, Integer examine) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        PageBean<Creation> pb = creationService.myList(pageNum, pageSize, user_id, examine);
        return Result.success(pb);
    }

    /**
     * 分页获取他人发布的图文列表
     **/
    @GetMapping("/otherList")
    public Result<PageBean<Creation>> otherList(Integer pageNum, Integer pageSize,@RequestParam @NotNull Integer user_id) {
        PageBean<Creation> pb = creationService.otherList(pageNum, pageSize, user_id);
        return Result.success(pb);
    }


    /**
     * 多参数分页查询图文内容
     **/
    @GetMapping("/list")
    public Result<PageBean<Creation>> list(Integer pageNum, Integer pageSize, @RequestParam(required = false) String title, @RequestParam(required = false) Integer class_id, @RequestParam(required = false) Integer tag_id) {
        PageBean<Creation> pb = creationService.list(pageNum, pageSize, title, class_id, tag_id);
        return Result.success(pb);
    }

    /**
     * 删除自己发布的图文内容
     **/
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull Integer creation_id) {
        creationService.del(creation_id);
        return Result.success();
    }

    /**
     * 获取图文关联的标签
     **/
    @GetMapping("/getTagsByCId")
    public Result<List<Tag>> getTagsByCId(@RequestParam @NotNull Integer creation_id) {
        List<Tag> list = creationService.getTagsByCId(creation_id);
        return Result.success(list);
    }

    /**
     * 关联标签
     **/
    @PostMapping("/connectTag")
    public Result connectTag(@NotNull Integer creation_id, @NotNull Integer tag_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(manager_id);
        if (user.getRole() == 1) {
            creationService.connectTag(creation_id, tag_id);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     * 取消关联标签
     **/
    @DeleteMapping("/cancelConnect")
    public Result cancelConnect(@RequestParam @NotNull Integer creation_id, @NotNull Integer tag_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            creationService.cancelConnect(creation_id, tag_id);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     * 多参数分页查询审核图文
     **/
    @GetMapping("/listToExamine")
    public Result<PageBean<Creation>> listToExamine(Integer pageNum, Integer pageSize, @RequestParam(required = false) String title, @RequestParam(required = false) Integer class_id, @RequestParam(required = false) Integer tag_id, @RequestParam @NotNull Integer examine) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            PageBean<Creation> pb = creationService.listToExamine(pageNum, pageSize, title, class_id, tag_id, examine);
            return Result.success(pb);
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     * 审核图文内容
     **/
    @PatchMapping("/examine")
    public Result examine(@RequestParam @NotNull Integer creation_id, @NotNull @Min(0) @Max(1) Integer examine, String review_comments) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            if (examine == 1 && review_comments == null) {
                return Result.error("打回图文请给出打回原因！");
            }
            creationService.examine(creation_id, examine, review_comments);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     * 更改分类
     **/
    @PatchMapping("/changeClass")
    public Result changeClass(@RequestParam @NotNull Integer creation_id, @NotNull Integer class_id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            creationService.changeClass(creation_id,class_id);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }
}
