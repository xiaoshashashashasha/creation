package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.mapper.CreationMapper;
import cn.edu.tust.beauty_back.service.CreationService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreationServiceImpl implements CreationService {

    @Autowired
    private CreationMapper creationMapper;

    @Override
    public void add(Creation creation) {
        //从线程中获取创建者id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creation.setUser_id(user_id);
        creationMapper.add(creation);
    }

    @Override
    public void update(Creation creation) {
        //从线程中获取创建者id,确保更改者与作者为同一人
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creation.setUser_id(user_id);
        creationMapper.update(creation);
    }

    @Override
    public Creation getCreationByCId(int creation_id) {
        Creation creation = creationMapper.getCreationByCId(creation_id);
        return creation;
    }

    @Override
    public PageBean<Creation> list(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id) {
        return null;
    }

    @Override
    public void del(int creation_id) {
        //从线程中获取创建者id,确保删除者与作者为同一人
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creationMapper.del(creation_id,user_id);
    }
}
