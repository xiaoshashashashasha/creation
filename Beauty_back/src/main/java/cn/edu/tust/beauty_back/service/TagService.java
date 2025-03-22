package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Tag;

import java.util.List;

public interface TagService {
    //新增标签
    void add(Tag tag);
    //无分页获取所有标签
    List<Tag> list();
    //删除标签
    void del(Integer tag_id);
}
