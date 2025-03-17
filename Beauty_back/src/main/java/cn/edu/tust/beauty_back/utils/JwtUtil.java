package cn.edu.tust.beauty_back.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    // 密钥
    private static final String SECRET = "sharkguragura";
    // 过期时间（24小时）
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * 生成Token
     */
    public static String genToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRATION_TIME);
        return JWT.create()
                .withClaim("claims", claims)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 接收Token并验证，返回业务数据
     */
    public static Map<String, Object> parseToken(String Token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(Token)
                .getClaim("claims")
                .asMap();
    }
}
