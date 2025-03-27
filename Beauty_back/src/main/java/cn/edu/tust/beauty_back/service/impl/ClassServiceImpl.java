package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Class;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;
import cn.edu.tust.beauty_back.mapper.ClassMapper;
import cn.edu.tust.beauty_back.service.ClassService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public void add(Class c) {
        classMapper.add(c);
    }

    @Override
    public PageBean<Class> list(Integer pageNum, Integer pageSize) {
        PageBean<Class> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Class> list = classMapper.list();

        Page<Class> p = (Page<Class>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public Class findClassById(int class_id) {
        Class cs = classMapper.findClassById(class_id);
        return cs;
    }

    @Override
    public void delClassByID(int class_id) {
        classMapper.delClassById(class_id);
    }

    @Override
    public Class findByClassName(String class_name) {
        return classMapper.findByClassName(class_name);
    }
}
