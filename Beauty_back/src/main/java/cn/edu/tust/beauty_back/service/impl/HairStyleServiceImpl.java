package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.HairStyle;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.mapper.HairStyleMapper;
import cn.edu.tust.beauty_back.service.HairStyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HairStyleServiceImpl implements HairStyleService {

    @Autowired
    private HairStyleMapper hairStyleMapper;

    @Override
    public void add(HairStyle hairStyle) {
        hairStyleMapper.add(hairStyle);
    }

    @Override
    public PageBean<HairStyle> list(Integer pageNum, Integer pageSize, String keyWord) {

        //创建PageBean对象
        PageBean<HairStyle> pb = new PageBean<>();

        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        //调用Mapper
        List<HairStyle> hs = hairStyleMapper.list(keyWord);

        //Page中提供了可获取PageHelper分页查询后，得到的总记录条数和当前页数据
        Page<HairStyle> p = (Page<HairStyle>) hs;

        //将数据填充到PageBean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public HairStyle getHairStyleById(int hairstyle_id) {

        HairStyle hairStyle = hairStyleMapper.getHairStyleById(hairstyle_id);
        return hairStyle;
    }

    @Override
    public void update(HairStyle hairStyle) {
        hairStyleMapper.update(hairStyle);
    }

    @Override
    public void del(Integer hairstyle_id) {
        hairStyleMapper.del(hairstyle_id);
    }

}
