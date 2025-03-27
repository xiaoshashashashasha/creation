package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    //新增标签
    @Insert("insert into beauty_tags(tag_name,created_at)" +
            "values(#{tag_name},now())")
    void add(Tag tag);

    //获取所有标签
    @Select("select * from beauty_tags")
    List<Tag> list();

    //根据id删除标签
    @Delete("delete from beauty_tags where tag_id = #{tag_id}")
    void del(Integer tag_id);

    //根据id获取tag
    @Select("select * from beauty_tags where tag_id = #{tag_id}")
    Tag findTagById(Integer tag_id);

    //根据标签名获取标签
    @Select("select * from beauty_tags where tag_name = #{tag_name}")
    Tag findTagByName(String tag_name);
}
