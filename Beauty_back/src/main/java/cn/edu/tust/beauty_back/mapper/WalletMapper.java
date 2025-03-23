package cn.edu.tust.beauty_back.mapper;

import cn.edu.tust.beauty_back.bean.Wallet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WalletMapper {

    //创建钱包
    @Insert("insert into beauty_wallet(user_id,created_at,updated_at)" +
            "values(#{user_id},now(),now())")
    void add(int user_id);

    //根据用户id查询钱包
    @Select("select * from beauty_wallet where user_id = #{user_id}")
    Wallet findWalletByUId(int user_id);
}
