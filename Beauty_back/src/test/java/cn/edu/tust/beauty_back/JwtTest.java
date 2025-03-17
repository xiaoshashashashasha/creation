package cn.edu.tust.beauty_back;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "张三");

        //生成jwt代码
        String Token = JWT.create()
                .withClaim("user", claims) //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) //添加过期时间
                .sign(Algorithm.HMAC256("sharkguragura")); //指定算法，配置密钥

        System.out.println(Token);
    }

    @Test
    public void testParse() {
        //定义字符串，模拟传递的token
        String Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3NDIyNTcwNDN9" +
                ".nYb8PmETwtfF0ZOBa8b2-XgcChx07j7FQ4tVgVBR_Z0";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("sharkguragura")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(Token); //验证Token,生成一个解析后的JWT对象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

        //导致验证失败的原因有三种
        //1.头部或载荷被篡改
        //2.密钥错误
        //3.令牌过期
    }
}
