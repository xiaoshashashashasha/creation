package cn.edu.tust.beauty_back.service.impl;


import cn.edu.tust.beauty_back.bean.*;
import cn.edu.tust.beauty_back.mapper.OfflineMapper;
import cn.edu.tust.beauty_back.mapper.UserMapper;
import cn.edu.tust.beauty_back.service.OfflineService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineServiceImpl implements OfflineService {
    @Autowired
    private OfflineMapper offlineMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addRequest(OfflineRequest offlineRequest) {
        offlineMapper.addRequest(offlineRequest);
    }

    @Override
    public PageBean<OfflineRequest> requestList(Integer pageNum, Integer pageSize, Integer manager_id) {
        PageBean<OfflineRequest> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<OfflineRequest> list = offlineMapper.requestList(manager_id);
        Page<OfflineRequest> p = (Page<OfflineRequest>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public PageBean<Offline> offlineList(Integer pageNum, Integer pageSize, String offline_city) {
        PageBean<Offline> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Offline> list = offlineMapper.offlineList(offline_city);
        Page<Offline> p = (Page<Offline>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public PageBean<Offline> myOfflineList(Integer pageNum, Integer pageSize, Integer manager_id) {
        PageBean<Offline> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Offline> list = offlineMapper.myOfflineList(manager_id);
        Page<Offline> p = (Page<Offline>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Offline offlineInfo(Integer offline_id) {
        return offlineMapper.offlineInfo(offline_id);
    }

    @Override
    public void improveOffline(Offline offline) {
        offlineMapper.improveOffline(offline);
    }

    @Override
    public Integer addMember(OfflineMember offlineMember) {
        //根据username获取user_id
        User user = userMapper.findByUserName(offlineMember.getUsername());
        if (user == null) {
            return 1;
        }
        offlineMember.setUser_id(user.getUser_id());

        User u = offlineMapper.findMemberByUId(offlineMember.getOffline_id(), offlineMember.getUser_id());
        if (u == null) {
            offlineMapper.addMember(offlineMember);
            return 0;
        }


        return 2;
    }

    @Override
    public List<OfflineMember> memberList(Integer offline_id) {
        List<OfflineMember> list = offlineMapper.memberList(offline_id);
        for (OfflineMember offlineMember : list) {
            User user = userMapper.findByUserId(offlineMember.getUser_id());
            offlineMember.setUsername(user.getUsername());
            offlineMember.setNickname(user.getNickname());
        }

        return list;
    }

    @Override
    public void delMember(Integer member_id) {
        offlineMapper.delMember(member_id);
    }

    @Override
    public PageBean<OfflineRequest> requestListExa(Integer pageNum, Integer pageSize) {
        PageBean<OfflineRequest> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<OfflineRequest> list = offlineMapper.requestListExa();

        Page<OfflineRequest> p = (Page<OfflineRequest>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public void examine(Integer request_id, Integer examine, String review_comments) {
        offlineMapper.updateExamine(request_id,examine,review_comments);
        if (examine == 0) {
            OfflineRequest offlineRequest = offlineMapper.requestInfo(request_id);
            offlineMapper.addOffline(offlineRequest.getManager_id(),offlineRequest.getTarget_city());
        }

    }

    @Override
    public void delOffline(Integer offline_id) {
        offlineMapper.delOffline(offline_id);
    }

}
