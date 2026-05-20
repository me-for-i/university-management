package com.university.management.exception;

import com.university.management.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 使用 @RestControllerAdvice 注解拦截所有 Controller 抛出的异常
 * 演示：异常处理、Stream API、Map 集合操作
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // SLF4J 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的 ResourceNotFoundException
     * @param ex 异常对象
     * @return 统一格式的错误响应
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("资源未找到: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.notFound(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理参数校验异常
     * 演示：Stream API 将校验错误收集为 Map
     *
     * @param ex 参数校验异常
     * @return 包含所有字段错误信息的响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // 使用 Stream API 将所有校验错误提取为 Map<字段名, 错误消息>
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()                                    // 获取所有字段错误
                .stream()                                            // 转为 Stream
                .collect(Collectors.toMap(                           // 收集为 Map
                        FieldError::getField,                        // 方法引用：获取字段名
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "校验失败" : fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing          // 合并策略：保留第一个
                ));

        logger.warn("参数校验失败: {}", errors);
        ApiResponse<Map<String, String>> response = new ApiResponse<>(400, "参数校验失败", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("非法参数: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.badRequest(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理访问不存在的路径（404）
     * 比如访问 / 或某个不存在的接口时触发
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        logger.warn("访问了不存在的路径: {}", ex.getMessage());
        String message = "请求的路径不存在（" + ex.getMessage() + "），可用接口请访问 http://localhost:8088/";
        ApiResponse<Void> response = ApiResponse.notFound(message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 兜底异常处理器：处理所有未被上面方法捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        logger.error("系统内部错误: ", ex);
        ApiResponse<Void> response = ApiResponse.error(500, "系统内部错误，请联系管理员");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
