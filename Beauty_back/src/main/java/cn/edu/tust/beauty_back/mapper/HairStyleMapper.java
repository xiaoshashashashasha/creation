package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.HairStyle;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HairStyleMapper {

    //新增发型
    @Insert("insert into beauty_hairstyle(hairstyle_name,content,hairstyle_pic,created_at,updated_at)" +
            "values(#{hairstyle_name},#{content},#{hairstyle_pic},now(),now())")
    void add(HairStyle hairStyle);

    //分页查询所有发型,若有关键词，则根据关键词查询
    List<HairStyle> list(String keyWord);

    //根据id查询发型
    @Select("select * from beauty_hairstyle where hairstyle_id = #{hairstyle_id}")
    HairStyle getHairStyleById(int hairstyle_id);

    //更新发型信息
    @Update("update beauty_hairstyle set hairstyle_name = #{hairstyle_name}, content = #{content}, updated_at = now() where hairstyle_id = #{hairstyle_id}")
    void update(HairStyle hairstyle);

    //删除发型信息
    @Delete("delete from beauty_hairstyle where hairstyle_id = #{hairstyle_id}")
    void del(Integer hairstyle_id);

    //上传发型封面
    @Update("update beauty_hairstyle set hairstylee_pic = #{coverUrl} where hairstyle_id = #{hairstyle_id}")
    void updateCover(String coverUrl, Integer hairstyle_id);
}
