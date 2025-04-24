package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Reservation;
import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.service.ReservationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    //带参获取我的预约列表
    @GetMapping("/myList")
    public Result<PageBean<Reservation>> myList(@RequestParam Integer pageNum, Integer pageSize,@NotNull @Min(0) @Max(3) Integer status) {
        return reservationService.myList(pageNum,pageSize,status);

    }

    //新建预约
    @PostMapping("/add")
    public Result add(@Validated Reservation reservation) {
        if (reservation.getStart_at().isAfter(reservation.getEnd_at())) {
            return Result.error("无效时间！");
        }
        return reservationService.add(reservation);
    }

    //取消预约
    @DeleteMapping("/del")
    public Result del(@RequestParam @NotNull Integer reservation_id) {
        return reservationService.del(reservation_id);
    }

    //评价
    @PatchMapping("/evaluate")
    public Result evaluate(@RequestBody @NotNull Integer reservation_id, @NotNull @Min(0) @Max(5) Integer point, @NotNull String comment) {
        return reservationService.evaluate(reservation_id,point,comment);
    }

    //分页获取门店评价列表
    @GetMapping("/commentList")
    public Result<PageBean<Reservation>> commentList(@RequestParam Integer pageNum, Integer pageSize, @NotNull Integer offline_id) {
        return reservationService.commentList(pageNum,pageSize,offline_id);
    }

    //分页获取门店下预约列表
    @GetMapping("/offlineList")
    public Result<PageBean<Reservation>> offlineList(@RequestParam Integer pageNum, Integer pageSize, @NotNull Integer offline_id, @NotNull @Min(0) @Max(3) Integer status) {
        return reservationService.offlineList(pageNum,pageSize,offline_id,status);
    }

    //核销预约
    @PatchMapping("/updateStatus")
    public Result updateStatus(@RequestBody Integer reservation_id, @NotNull @Min(1) @Max(2) Integer status) {
        return reservationService.updateStatus(reservation_id,status);
    }
}
