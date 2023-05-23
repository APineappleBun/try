package com.example.orienteering.config;

import com.example.orienteering.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    static final String IMG_PATH = System.getProperty("user.dir") + "/myResources/image/"; // 图片路径
    static final String AVATAR_PATH = System.getProperty("user.dir") + "/myResources/avatar/"; // 图片路径

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 添加图片资源
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + IMG_PATH);
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:" + AVATAR_PATH);
    }

    @Bean
    public SysInterceptor sysInterceptor(){
        return new SysInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        String[] patterns=new String[]{"/adminLogin","/product/**","/bigType/**","/users/wxlogin","/weixinpay/**"};
        String[] patterns = new String[] {"/achievement/my/**", "/progress/my/**", "/puzzle/my/**",  "/society/my/**", "/theme/my/**", "/user/my/**", "/userQuest/my/**", };
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns(patterns)
                .excludePathPatterns();
    }



}

