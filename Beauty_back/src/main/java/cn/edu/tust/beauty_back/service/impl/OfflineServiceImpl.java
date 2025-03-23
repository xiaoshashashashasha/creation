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
    public List<OfflineRequest> requestList(Integer manager_id) {
        return offlineMapper.requestList(manager_id);
    }

    @Override
    public OfflineRequest requestInfo(Integer request_id) {
        return offlineMapper.requestInfo(request_id);
    }

    @Override
    public List<Offline> offlineList(Integer manager_id) {
        return offlineMapper.offlineList(manager_id);
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
    public void addMember(OfflineMember offlineMember) {
        //根据user_name获取user_id
        User user = userMapper.findByUserName(offlineMember.getUser_name());
        offlineMember.setUser_id(user.getUser_id());
        offlineMapper.addMember(offlineMember);
    }

    @Override
    public List<OfflineMember> memberList(Integer offline_id) {
        return offlineMapper.memberList(offline_id);
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
            offlineMapper.addOffline(offlineRequest.getManager_id());
        }

    }

    @Override
    public void delOffline(Integer offline_id) {
        offlineMapper.delOffline(offline_id);
    }
}
