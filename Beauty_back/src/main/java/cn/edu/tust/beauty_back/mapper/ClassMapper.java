package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Class;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassMapper {
    //新增分类
    @Insert("insert into beauty_classes(class_name,created_at)" +
            "values(#{class_name},now())")
    void add(Class c);

    //查询所有分类
    @Select("select * from beauty_classes")
    List<Class> list();

    //根据id查询分类
    @Select("select * from beauty_classes where class_id = #{class_id}")
    Class findClassById(int class_id);

    //根据id删除分类
    @Delete("delete from beauty_classes where class_id = #{class_id}")
    void delClassById(int class_id);

    //根据分类名查找分类
    @Select("select * from beauty_classes where class_name = #{class_name}")
    Class findByClassName(String class_name);
}
