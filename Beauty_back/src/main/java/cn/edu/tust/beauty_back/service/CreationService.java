package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;

public interface CreationService {
    //发布图文内容
    void add(Creation creation);
    //编辑图文内容
    void update(Creation creation);
    //根据CId获取图文内容
    Creation getCreationByCId(int creation_id);
    //多参数分页查询图文内容
    PageBean<Creation> list(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id);
    //删除自己发布的图文内容
    void del(int creation_id);
}
