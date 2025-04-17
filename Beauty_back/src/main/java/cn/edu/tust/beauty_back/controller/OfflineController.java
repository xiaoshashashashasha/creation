package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.OfflineService;
import cn.edu.tust.beauty_back.service.UserService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/offline")
public class OfflineController {

    @Autowired
    private OfflineService offlineService;
    @Autowired
    private UserService userService;

    /**
     *申请创建线下门店
     * **/
    @PostMapping("/addRequest")
    public Result  addRequest(@RequestBody @Validated OfflineRequest offlineRequest){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        offlineRequest.setManager_id(manager_id);
        offlineService.addRequest(offlineRequest);
        return Result.success();
    }

    /**
     *查看我的门店申请
     * **/
    @GetMapping("/requestList")
    public Result<PageBean<OfflineRequest>> requestList(Integer pageNum, Integer pageSize){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        PageBean<OfflineRequest> pb = offlineService.requestList(pageNum, pageSize, manager_id);
        return Result.success(pb);
    }


    /**
     *多参数分页获取门店列表
     * **/
    @GetMapping("/offlineList")
    public  Result<PageBean<Offline>> offlineList(Integer pageNum, Integer pageSize, @RequestParam(required = false) String offline_city){
        PageBean<Offline> pb = offlineService.offlineList(pageNum, pageSize, offline_city);
        return Result.success(pb);
    }


    /**
     *查看我的门店列表
     * **/
    @GetMapping("/myOfflineList")
    public Result<PageBean<Offline>> myOfflineList(Integer pageNum, Integer pageSize){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        PageBean<Offline> pb = offlineService.myOfflineList(pageNum, pageSize, manager_id);
        return Result.success(pb);
    }

    /**
     *查看门店详情
     * **/
    @GetMapping("/offlineInfo")
    public Result offlineInfo(@RequestParam @NotNull Integer offline_id){
        Offline offline = offlineService.offlineInfo(offline_id);
        return Result.success(offline);
    }


    /**
     *完善门店信息
     * **/
    @PatchMapping("/improveOffline")
    public Result improveOffline(@RequestBody @Validated Offline offline){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        Offline o = offlineService.offlineInfo(offline.getOffline_id());
        if (o.getManager_id() == user_id) {
            offlineService.improveOffline(offline);
            return Result.success();
        }
        return Result.error("您无权执行该操作！");
    }

    /**
     *置入门店成员
     * **/
    @PostMapping("/addMember")
    public Result addMember(@RequestBody @Validated OfflineMember offlineMember){
        Integer code = offlineService.addMember(offlineMember);
        if (code == 0){
            return Result.success();
        }else if (code == 1){
            return Result.error("用户不存在！");
        }
        return Result.error("该成员已存在！");
    }

    /**
     *获取成员列表
     * **/
    @GetMapping("/memberList")
    public Result<List<OfflineMember>> memberList(@RequestParam @NotNull Integer offline_id){
        List<OfflineMember> list = offlineService.memberList(offline_id);
        return Result.success(list);
    }

    /**
     *删除门店成员
     * **/
    @DeleteMapping("/delMember")
    public Result delMember(@RequestParam @NotNull Integer member_id){
        offlineService.delMember(member_id);
        return Result.success();
    }

    /**
     *分页获取门店申请
     * **/
    @GetMapping("/requestListExa")
    public Result<PageBean<OfflineRequest>> requestListExa(Integer pageNum, Integer pageSize){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if(user.getRole() == 1) {

            PageBean<OfflineRequest> pb = offlineService.requestListExa(pageNum,pageSize);
            return Result.success(pb);
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     *审核门店申请，并创建门店框架
     * **/
    @PatchMapping("/examine")
    public Result examine(@NotNull Integer request_id, @NotNull @Min(0) @Max(1) Integer examine, String review_comments){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            if (examine == 1 && review_comments == null){
                return Result.error("拒绝申请请给出拒绝原因！");
            }
            offlineService.examine(request_id,examine,review_comments);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }

    /**
     *删除门店
     * **/
    @DeleteMapping("/delOffline")
    public Result delOffline(@RequestParam @NotNull Integer offline_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        Offline offline = offlineService.offlineInfo(offline_id);
        if (offline.getManager_id() == user_id) {
            offlineService.delOffline(offline_id);
            return Result.success();
        }
        return Result.error("您无权执行该操作！");
    }
}
