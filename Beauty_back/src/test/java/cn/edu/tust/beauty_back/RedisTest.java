package cn.edu.tust.beauty_back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest //此注解可在该单元测试执行前，初始化Spring容器
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet() {
        //向Redis中存储一个键值对 StringRedisTemplate
        ValueOperations<String ,String> operations = stringRedisTemplate.opsForValue();

        operations.set("username", "shark");
        operations.set("id", "1",10, TimeUnit.SECONDS);
    }

    @Test
    public void testGet(){
        //从Redis中获取一个键值对
        ValueOperations<String ,String> operations = stringRedisTemplate.opsForValue();

        System.out.println(operations.get("username"));
    }
}
