package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 根路径控制器
 * 访问 http://localhost:8088/ 时返回 API 导航信息
 */
@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, Object>>> root() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("项目名称", "大学管理系统");
        info.put("版本", "1.0.0");
        info.put("API基础路径", "/api");

        // 列出所有可用接口
        Map<String, String> apis = new LinkedHashMap<>();
        apis.put("POST /api/auth/login", "用户登录（参数: username, password）");
        apis.put("GET /api/departments", "查询所有部门");
        apis.put("GET /api/teachers", "查询所有教师");
        apis.put("GET /api/students", "查询所有学生");
        apis.put("GET /api/courses", "查询所有课程");
        apis.put("GET /api/grades", "查询所有成绩");
        info.put("可用接口", apis);

        return ResponseEntity.ok(ApiResponse.success("欢迎使用大学管理系统", info));
    }
}
