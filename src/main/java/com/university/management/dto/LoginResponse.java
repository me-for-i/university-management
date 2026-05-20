package com.university.management.dto;

import com.university.management.enums.UserRole;

/**
 * 登录响应 DTO（使用 record 类型）
 *
 * @param userId   用户ID
 * @param username 用户名
 * @param realName 真实姓名
 * @param role     用户角色
 * @param message  提示信息
 */
public record LoginResponse(
        Long userId,
        String username,
        String realName,
        UserRole role,
        String message
) {
}
