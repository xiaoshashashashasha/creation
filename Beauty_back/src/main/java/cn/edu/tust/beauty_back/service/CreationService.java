package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;

import java.util.List;

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

    //多参数分页查询审核图文
    PageBean<Creation> listToExamine(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id, Integer examine);

    //获取图文关联的标签
    List<Tag> getTagsByCId(Integer creation_id);

    //关联标签
    void connectTag(Integer creation_id, Integer tag_id);
    //取消关联标签
    void cancelConnect(Integer creation_id, Integer tag_id);
    //审核图文内容
    void examine(Integer creation_id, Integer examine);
}
