package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.service.OfflineService;
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
    public Result<List<OfflineRequest>> requestList(){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        List<OfflineRequest> list = offlineService.requestList(manager_id);
        return Result.success(list);
    }

    /**
     *查看申请详情
     * **/
    @GetMapping("/requestInfo")
    public Result requestInfo(Integer request_id){
        OfflineRequest offlineRequest = offlineService.requestInfo(request_id);
        return Result.success(offlineRequest);
    }

    /**
     *查看我的门店列表
     * **/
    @GetMapping("/offlineList")
    public Result<List<Offline>> offlineList(){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");
        List<Offline> list = offlineService.offlineList(manager_id);
        return Result.success(list);
    }

    /**
     *查看门店详情
     * **/
    @GetMapping("/offlineInfo")
    public Result offlineInfo(Integer offline_id){
        Offline offline = offlineService.offlineInfo(offline_id);
        return Result.success(offline);
    }

    /**
     *完善门店信息
     * **/
    @PatchMapping("/improveOffline")
    public Result improveOffline(@RequestBody @Validated Offline offline){
        offlineService.improveOffline(offline);
        return Result.success();
    }

    /**
     *置入门店成员
     * **/
    @PostMapping("/addMember")
    public Result addMember(@RequestParam @Validated OfflineMember offlineMember){
        offlineService.addMember(offlineMember);
        return Result.success();
    }

    /**
     *获取成员列表
     * **/
    @GetMapping("/memberList")
    public Result<List<OfflineMember>> memberList(Integer offline_id){
        List<OfflineMember> list = offlineService.memberList(offline_id);
        return Result.success(list);
    }

    /**
     *删除门店成员
     * **/
    @DeleteMapping("/delMember")
    public Result delMember(@RequestParam Integer member_id){
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
    public Result delOffline(@RequestParam Integer offline_id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        User user = userService.findByUserId(user_id);
        if (user.getRole() == 1) {
            offlineService.delOffline(offline_id);
            return Result.success();
        }
        return Result.error("您无权访问该内容！");
    }
}
