package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("select * from beauty_user where username=#{username}")
    User findByUserName(String username);

    //添加新用户
    @Insert("insert into beauty_user(username,jwt_password,nickname,gender,email,created_at,updated_at)" +
            " values(#{username},#{jwt_password},#{nickname},#{gender},#{email},now(),now())")
    void add(String username, String jwt_password,String nickname, String gender, String email);

    //更新用户信息
    @Update("update beauty_user set nickname=#{nickname},email=#{email},updated_at=now() where user_id=#{user_id}")
    void update(User user);

    //更新用户头像url
    @Update("update beauty_user set user_pic=#{avatarUrl},updated_at=now() where user_id=#{user_id}")
    void updateAvatar(Integer user_id,String avatarUrl);

    //更新用户密码
    @Update("update beauty_user set jwt_password=#{jwt_password} where user_id=#{user_id}")
    void updatePwd(Integer user_id,String jwt_password);
}
