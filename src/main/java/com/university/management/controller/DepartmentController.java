package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.entity.Department;
import com.university.management.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 演示：完整的 RESTful CRUD 接口设计
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取所有部门
     * GET /api/departments
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> findAll() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    /**
     * 根据ID获取部门
     * GET /api/departments/{id}
     *
     * @PathVariable 将 URL 路径中的变量绑定到方法参数
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> findById(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    /**
     * 根据编码获取部门
     * GET /api/departments/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<Department>> findByCode(@PathVariable String code) {
        Department department = departmentService.findByCode(code);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    /**
     * 创建新部门
     * POST /api/departments
     *
     * @ResponseStatus 注解设置成功时的 HTTP 状态码为 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Department>> create(@RequestBody Department department) {
        Department created = departmentService.create(department);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("部门创建成功", created));
    }

    /**
     * 更新部门
     * PUT /api/departments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> update(@PathVariable Long id,
                                                          @RequestBody Department department) {
        Department updated = departmentService.update(id, department);
        return ResponseEntity.ok(ApiResponse.success("部门更新成功", updated));
    }

    /**
     * 删除部门
     * DELETE /api/departments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("部门删除成功"));
    }
}
