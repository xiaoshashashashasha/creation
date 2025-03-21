package cn.edu.tust.beauty_back.interceptors;

import cn.edu.tust.beauty_back.utils.JwtUtil;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String Token = request.getHeader("Authorization");

        //验证Token
        try {
            //解析Token，获取数据
            Map<String, Object> claims = JwtUtil.parseToken(Token);

            //将业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);

            return true;
        } catch (Exception e) {

            //http响应状态码为401
            response.setStatus(401);

            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
