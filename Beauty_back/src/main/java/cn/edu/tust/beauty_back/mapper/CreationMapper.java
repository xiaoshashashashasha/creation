package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Creation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CreationMapper {

    //发布图文内容
    @Insert("insert into beauty_creation(user_id,title,abs_text,cover_pic,content,class_id,created_at,updated_at)" +
            "values(#{user_id},#{title},#{abs_text},#{cover_pic},#{content},#{class_id},now(),now())")
    void add(Creation creation);

    //编辑图文内容
    @Update("update beauty_creation set title = #{title}, abs_text = #{abs_text}, cover_pic = #{cover_pic}, content = #{content}, class_id = #{class_id}, updated_at = now() where creation_id = #{creation_id} and user_id = #{user_id}")
    void update(Creation creation);

    //根据标签id获取图文内容
    @Select("select * from beauty_creation where creation_id = #{creation_id} and examine = 0")
    Creation getCreationByCId(int creation_id);

    //删除自己发布的图文内容
    @Delete("delete from beauty_creation where creation_id = #{creation_id} and user_id = #{user_id}")
    void del(int creation_id, Integer user_id);

    //获取图文关联的标签id
    @Select("select tag_id from creation_tags where creation_id = #{creation_id}")
    List<Integer> getTIdByCId(Integer creation_id);

    //关联标签
    @Insert("insert into creation_tags(creation_id,tag_id,created_at)" +
            "values(#{creation_id},#{tag_id},now())")
    void connectTag(Integer creation_id, Integer tag_id);

    //取消关联标签
    @Delete("delete from creation_tags where creation_id = #{creation_id} and tag_id = #{tag_id}")
    void cancelConnect(Integer creation_id, Integer tag_id);

    //根据标签id获取关联图文id
    @Select("select creation_id from creation_tags where tag_id = #{tag_id}")
    List<Integer> getCIdByTId(Integer tag_id);

    //查询图文内容，若有条件则应用条件
    List<Creation> list(String title, Integer class_id);

    //审核用图文查询，若有条件则应用条件
    List<Creation> listToExamine(String title, Integer class_id, Integer examine);

    //审核图文内容
    @Update("update beauty_creation set examine = #{examine}, review_comments = #{review_comments} where creation_id = #{creation_id}")
    void updateExamine(Integer creation_id, Integer examine, String review_comments);

    //获取我发布的图文列表
    @Select("select * from beauty_creation where user_id = #{user_id}")
    List<Creation> myList(Integer user_id);
}
