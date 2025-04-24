package cn.edu.tust.beauty_back.service;


import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Reservation;
import cn.edu.tust.beauty_back.bean.Result;

public interface ReservationService {

    //分页获取我的预约
    Result<PageBean<Reservation>> myList(Integer pageNum, Integer pageSize, Integer status);

    //新建预约
    Result add(Reservation reservation);

    //取消预约
    Result del(Integer reservation_id);

    //评价
    Result evaluate(Integer reservation_id, Integer point, String comment);

    //分页获取门店评价列表
    Result<PageBean<Reservation>> commentList(Integer pageNum, Integer pageSize, Integer offline_id);

    //分页获取门店下预约列表
    Result<PageBean<Reservation>> offlineList(Integer pageNum, Integer pageSize, Integer offline_id, Integer status);

    //核销预约
    Result updateStatus(Integer reservation_id, Integer status);
}
