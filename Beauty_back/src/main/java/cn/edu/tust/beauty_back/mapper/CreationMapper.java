package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Creation;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CreationMapper {

    //发布图文内容
    @Insert("insert into beauty_creation(user_id,title,abstract,cover_pic,content,class_id,created_at,updated_at)" +
            "values(#{user_id},#{title},#{Abstract},#{cover_pic},#{content},#{class_id},now(),now())")
    void add(Creation creation);

    //编辑图文内容
    @Update("update beauty_creation set title = #{title}, abstract = #{Abstract}, cover_pic = #{cover_pic}, content = #{content}, class_id = #{class_id}, updated_at = now() where creation_id = #{creation_id} and user_id = #{user_id}")
    void update(Creation creation);

    //根据CId获取图文内容
    @Select("select * from beauty_creation where creation_id = #{creation_id}")
    Creation getCreationByCId(int creation_id);

    //删除自己发布的图文内容
    @Delete("delete from beauty_creation where creation_id = #{creation_id} and user_id = #{user_id}")
    void del(int creation_id, Integer user_id);
}
