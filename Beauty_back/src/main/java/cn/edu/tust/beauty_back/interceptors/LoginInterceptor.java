package cn.edu.tust.beauty_back.interceptors;

import cn.edu.tust.beauty_back.utils.JwtUtil;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 检查是否为 WebSocket 请求
        String upgradeHeader = request.getHeader("Upgrade");
        String connectionHeader = request.getHeader("Connection");
        if ("websocket".equalsIgnoreCase(upgradeHeader) &&
                connectionHeader != null && connectionHeader.toLowerCase().contains("upgrade")) {
            return true;
        }

        // 检查是否为放行路径
        String uri = request.getRequestURI();
        if (uri.equals("/api/user/register") || uri.equals("/api/user/login") ||
                uri.equals("/api/user/register/") || uri.equals("/api/user/login/")) {
            return true;
        }

        // 令牌验证
        String token = request.getHeader("Authorization");

        // 验证Token
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);

            if (redisToken == null) {
                System.out.println("Token失效: " + token);
                throw new RuntimeException("Token失效");
            }

            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            System.out.println("验证失败，返回401: " + uri);
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
