package com.university.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 大学管理系统 - Spring Boot 启动类
 *
 * @SpringBootApplication 是一个组合注解，等价于：
 *   - @Configuration：标记为配置类
 *   - @EnableAutoConfiguration：启用自动配置
 *   - @ComponentScan：扫描当前包及子包下的所有组件
 */
@SpringBootApplication
public class UniversityManagementApplication {

    /**
     * 程序入口方法
     * SpringApplication.run() 启动嵌入式 Tomcat 服务器并初始化 Spring 容器
     */
    public static void main(String[] args) {
        SpringApplication.run(UniversityManagementApplication.class, args);
        System.out.println("""
                ========================================
                  大学管理系统启动成功！
                  访问地址: http://localhost:8088
                  API 基础路径: /api
                ========================================
                """);
    }
}
