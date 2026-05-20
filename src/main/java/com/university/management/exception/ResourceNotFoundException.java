package com.university.management.exception;

/**
 * 自定义异常：资源未找到
 * 继承 RuntimeException，属于非受检异常
 * 当查询的实体不存在时抛出此异常
 */
public class ResourceNotFoundException extends RuntimeException {

    /** 资源类型名称 */
    private final String resourceName;

    /** 查询字段名 */
    private final String fieldName;

    /** 查询字段值 */
    private final Object fieldValue;

    /**
     * 构造方法
     * @param resourceName 资源名称（如"教师"、"学生"）
     * @param fieldName    查询字段名（如"id"、"学号"）
     * @param fieldValue   查询字段值
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // 调用父类构造方法，传入格式化的错误消息
        super(String.format("未找到%s [%s: %s]", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
