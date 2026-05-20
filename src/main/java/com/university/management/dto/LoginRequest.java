package com.university.management.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求 DTO
 * 使用 Java 17 的 record 类型（不可变数据载体）
 * record 自动生成：构造方法、getter、equals、hashCode、toString
 *
 * @param username 用户名
 * @param password 密码
 */
public record LoginRequest(
        @NotBlank(message = "用户名不能为空")
        String username,

        @NotBlank(message = "密码不能为空")
        String password
) {
}
