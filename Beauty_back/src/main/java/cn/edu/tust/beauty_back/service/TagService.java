package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;

import java.util.List;

public interface TagService {
    //新增标签
    void add(Tag tag);
    //分页获取所有标签
    PageBean<Tag> list(Integer pageNum, Integer pageSize);
    //删除标签
    void del(Integer tag_id);
    //根据标签名查找标签
    Tag findByTagName(String tag_name);
    //根据id查找标签
    Tag findByTagId(Integer tag_id);
}
