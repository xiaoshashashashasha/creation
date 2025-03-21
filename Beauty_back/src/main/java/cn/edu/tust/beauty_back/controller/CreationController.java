package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.service.CreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/creation")
public class CreationController {

    @Autowired
    private CreationService creationService;

    /**
     *发布图文内容
     * **/
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Creation creation) {
        creationService.add(creation);
        return Result.success();
    }

    /**
     *编辑图文内容
     * **/
    @PutMapping("/update")
    public Result update(@RequestBody @Validated Creation creation) {
        creationService.update(creation);
        return Result.success();
    }

    /**
     *获取详细内容
     * **/
    @GetMapping ("/creationInfo")
    public Result<Creation> creationInfo(@RequestParam int creation_id) {
        Creation creation = creationService.getCreationByCId(creation_id);
        return Result.success(creation);
    }

    /**
     *多参数分页查询图文内容
     * **/
    @GetMapping ("/list")
    public Result<PageBean<Creation>> list(Integer pageNum, Integer pageSize, @RequestParam(required = false) String title, @RequestParam(required = false) Integer class_id, @RequestParam(required = false) Integer tag_id) {
        PageBean<Creation> pb = creationService.list(pageNum,pageSize,title,class_id,tag_id);
        //实现思路：先检验tag_id,从关联表中获取相关creation_id,去creation表获取对应creation,放入list中
        //遍历list分别检验examine、class_id,再模糊匹配title
        //最后在service层将结果list转换为pagebean返回到controller
        return Result.success(pb);
    }

    /**
     *删除自己发布的图文内容
     * **/
    @DeleteMapping("/del")
    public Result del(@RequestParam int creation_id) {
        creationService.del(creation_id);
        return Result.success();
    }

    /**
     *关联标签
     * **/

    /**
     *取消关联标签
     * **/

    /**
     *审核图文内容
     * **/

    /**
     *打回图文内容
     * **/

}
