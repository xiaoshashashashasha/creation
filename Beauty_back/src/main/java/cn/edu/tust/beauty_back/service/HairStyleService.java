package cn.edu.tust.beauty_back.service;


import cn.edu.tust.beauty_back.bean.HairStyle;
import cn.edu.tust.beauty_back.bean.PageBean;

public interface HairStyleService {
    //新增发型
    void add(HairStyle hairStyle);
    //条件分页列表查询发型
    PageBean<HairStyle> list(Integer pageNum, Integer pageSize, String keyWord);
    //根据id获取发型
    HairStyle getHairStyleById(int hairstyle_id);
    //更新发型信息
    void update(HairStyle hairStyle);
    //删除发型信息
    void del(Integer hairstyle_id);
    //上传发型封面
    void updateCover(String coverUrl, Integer hairstyle_id);
}
