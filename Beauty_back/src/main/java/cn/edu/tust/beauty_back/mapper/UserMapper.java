package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user(username,jwt_password,gender,email,created_at,updated_at)" +
            " values(#{username},#{jwt_password},#{gender},#{email},now(),now())")
    void add(String username, String jwt_password, String gender, String email);
}
