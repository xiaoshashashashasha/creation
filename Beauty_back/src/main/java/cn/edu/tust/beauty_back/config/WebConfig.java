package cn.edu.tust.beauty_back.config;

import cn.edu.tust.beauty_back.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //无需验证登陆状态的请求不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/register","/user/login", "/ws/**");
    }
}
