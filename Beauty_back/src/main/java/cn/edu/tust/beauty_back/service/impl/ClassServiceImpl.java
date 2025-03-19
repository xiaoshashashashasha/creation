package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Class;
import cn.edu.tust.beauty_back.mapper.ClassMapper;
import cn.edu.tust.beauty_back.service.ClassService;
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
    public List<Class> list() {
        return classMapper.list();
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
}
