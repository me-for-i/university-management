package com.university.management.enums;

/**
 * 课程状态枚举
 * 表示课程的当前状态
 */
public enum CourseStatus {
    ACTIVE("开课中"),
    INACTIVE("未开课"),
    COMPLETED("已结课");

    private final String description;

    CourseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
