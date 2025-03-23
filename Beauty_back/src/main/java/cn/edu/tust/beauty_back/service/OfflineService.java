package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Offline;
import cn.edu.tust.beauty_back.bean.OfflineMember;
import cn.edu.tust.beauty_back.bean.OfflineRequest;
import cn.edu.tust.beauty_back.bean.PageBean;

import java.util.List;

public interface OfflineService {
    //申请创建线下门店
    void addRequest(OfflineRequest offlineRequest);
    //查看我的门店申请列表
    List<OfflineRequest> requestList(Integer manager_id);
    //查看申请详情
    OfflineRequest requestInfo(Integer request_id);
    //查看我的门店列表
    List<Offline> offlineList(Integer manager_id);
    //查看门店详情
    Offline offlineInfo(Integer offline_id);
    //完善门店信息
    void improveOffline(Offline offline);
    //置入门店成员
    void addMember(OfflineMember offlineMember);
    //获取成员列表
    List<OfflineMember> memberList(Integer offline_id);
    //删除门店成员
    void delMember(Integer member_id);
    //分页获取门店申请
    PageBean<OfflineRequest> requestListExa(Integer pageNum, Integer pageSize);
    //审核门店申请，并创建门店框架
    void examine(Integer request_id, Integer examine, String review_comments);
    //删除门店
    void delOffline(Integer offline_id);
}
