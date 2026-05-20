package com.university.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 演示：@Configuration 配置类、@Bean 方法、接口匿名实现
 */
@Configuration
public class WebConfig {

    /**
     * 配置跨域访问（CORS）
     * 允许前端应用跨域调用后端 API
     *
     * 返回 WebMvcConfigurer 的匿名内部类实现（也可以用 Lambda）
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")           // 匹配所有 /api/ 开头的路径
                        .allowedOrigins("*")              // 允许所有来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")  // 允许的 HTTP 方法
                        .allowedHeaders("*")              // 允许所有请求头
                        .maxAge(3600);                    // 预检请求缓存时间（秒）
            }
        };
    }

    /**
     * 配置 Jackson ObjectMapper
     * 处理 Java 8 日期时间序列化和 Hibernate 懒加载代理问题
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册 Java 8 时间模块，支持 LocalDate、LocalDateTime 等类型
        mapper.registerModule(new JavaTimeModule());
        // 注册 Hibernate 6 模块：序列化懒加载代理时自动返回 null，而不是报错
        mapper.registerModule(new Hibernate6Module());
        // 禁止将日期序列化为时间戳数字，改为 ISO-8601 格式字符串
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
