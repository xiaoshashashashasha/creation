package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.mapper.OfflineMapper;
import cn.edu.tust.beauty_back.mapper.ReservationMapper;
import cn.edu.tust.beauty_back.mapper.UserMapper;
import cn.edu.tust.beauty_back.service.ReservationService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    OfflineMapper offlineMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<PageBean<Reservation>> myList(Integer pageNum, Integer pageSize, Integer status) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        PageBean<Reservation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Reservation> list = reservationMapper.myList(user_id,status);
        //完善信息
        for (Reservation reservation : list) {
            Offline offline = offlineMapper.offlineInfo(reservation.getOffline_id());
            OfflineMember member = offlineMapper.findMemberByMId(reservation.getMember_id());

            reservation.setOffline_name(offline.getOffline_name());
            reservation.setMember_name(member.getMember_name());
            reservation.setMember_user_id(member.getUser_id());
        }

        Page<Reservation> p = (Page<Reservation>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return Result.success(pb);
    }

    @Override
    public Result add(Reservation reservation) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        reservation.setUser_id(user_id);
        reservationMapper.add(reservation);
        return Result.success();
    }

    @Override
    public Result del(Integer reservation_id) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        Reservation reservation = reservationMapper.Info(reservation_id);
        if (reservation.getUser_id() != user_id) {
            return Result.error("无权删除！");
        }
        reservationMapper.del(reservation_id);
        return Result.success();
    }

    @Override
    public Result evaluate(Integer reservation_id, Integer point, String comment) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");

        Reservation reservation = reservationMapper.Info(reservation_id);
        if (reservation.getUser_id() != user_id) {
            return Result.error("无权评价！");
        }

        reservationMapper.evalute(reservation_id, point, comment);
        return Result.success();
    }

    @Override
    public Result<PageBean<Reservation>> commentList(Integer pageNum, Integer pageSize, Integer offline_id) {
        PageBean<Reservation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Reservation> list = reservationMapper.commentList(offline_id);

        Page<Reservation> p = (Page<Reservation>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return Result.success(pb);
    }

    @Override
    public Result<PageBean<Reservation>> offlineList(Integer pageNum, Integer pageSize, Integer offline_id, Integer status) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");

        Offline offline = offlineMapper.offlineInfo(manager_id);

        if (offline.getManager_id() != manager_id) {
            return Result.error("无权访问！");
        }

        PageBean<Reservation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Reservation> list = reservationMapper.offlineList(offline_id,status);

        Page<Reservation> p = (Page<Reservation>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return Result.success(pb);
    }

    @Override
    public Result updateStatus(Integer reservation_id, Integer status) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer manager_id = (Integer) map.get("user_id");

        Offline offline = offlineMapper.offlineInfo(manager_id);

        if (offline.getManager_id() != manager_id) {
            return Result.error("无权访问！");
        }

        reservationMapper.updateStatus(reservation_id,status);
        return Result.success();
    }
}
