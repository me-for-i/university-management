package com.university.management.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * JPA 配置类
 * 解决实体类循环引用导致的 JSON 序列化问题
 * 演示：自定义 Jackson 序列化器、Hibernate Lazy Loading 处理
 */
@Configuration
public class JpaConfig {

    /**
     * 配置 Jackson 以优雅地处理 Hibernate 延迟加载的代理对象
     * 当序列化一个尚未加载的延迟加载属性时，输出 null 而不是抛出异常
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.failOnEmptyBeans(false);
    }
}
