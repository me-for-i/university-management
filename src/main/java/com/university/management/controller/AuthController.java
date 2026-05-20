package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.dto.LoginRequest;
import com.university.management.dto.LoginResponse;
import com.university.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 处理用户登录
 * 演示：REST API 设计、请求参数校验、统一响应格式
 *
 * @RestController = @Controller + @ResponseBody（自动序列化为 JSON）
 * @RequestMapping 定义基础路径
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口
     * POST /api/auth/login
     *
     * @Valid 注解触发 DTO 中的校验规则（@NotBlank 等）
     * @RequestBody 将 JSON 请求体反序列化为 Java 对象
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", loginResponse));
    }
}
