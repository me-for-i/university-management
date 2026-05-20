package com.university.management.enums;

/**
 * 用户角色枚举
 * 演示Java枚举类型的使用，包含属性、构造方法和方法
 */
public enum UserRole {
    // 枚举常量，每个常量关联一个中文描述
    ADMIN("管理员"),
    TEACHER("教师"),
    STUDENT("学生");

    // 枚举的属性字段
    private final String description;

    /**
     * 枚举构造方法（必须是 private 或包级别访问）
     * @param description 角色的中文描述
     */
    UserRole(String description) {
        this.description = description;
    }

    /**
     * 获取角色描述
     * @return 中文描述字符串
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据描述查找对应的枚举值
     * 演示：Stream API + Lambda + Optional 的组合使用
     *
     * @param description 中文描述
     * @return 对应的 UserRole 枚举值
     * @throws IllegalArgumentException 如果找不到对应的枚举值
     */
    public static UserRole fromDescription(String description) {
        return java.util.Arrays.stream(values())          // 将枚举数组转为 Stream
                .filter(role -> role.description.equals(description))  // Lambda 过滤
                .findFirst()                                            // 获取 Optional
                .orElseThrow(() -> new IllegalArgumentException(       // 异常处理
                        "未知的角色描述: " + description
                ));
    }
}
