package cn.edu.tust.beauty_back.service;

import cn.edu.tust.beauty_back.bean.Class;

import java.util.List;

public interface ClassService {
    //新增分类
    void add(Class c);

    //获取分类列表
    List<Class> list();

    //根据id查询分类
    Class findClassById(int class_id);

    //根据id删除分类
    void delClassByID(int class_id);
}
