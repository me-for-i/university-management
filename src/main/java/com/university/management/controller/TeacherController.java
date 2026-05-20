package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.entity.Teacher;
import com.university.management.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 教师管理控制器
 * 演示：RESTful CRUD + 高级查询接口
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 获取所有教师
     * GET /api/teachers
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Teacher>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(teacherService.findAll()));
    }

    /**
     * 根据ID获取教师
     * GET /api/teachers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.findById(id)));
    }

    /**
     * 根据工号获取教师
     * GET /api/teachers/employee/{employeeId}
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<Teacher>> findByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.findByEmployeeId(employeeId)));
    }

    /**
     * 根据姓名模糊搜索教师
     * GET /api/teachers/search?name=xxx
     *
     * @RequestParam 绑定查询参数（URL 中 ? 后的参数）
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Teacher>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.searchByName(name)));
    }

    /**
     * 根据部门ID查找教师
     * GET /api/teachers/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<Teacher>>> findByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.findByDepartmentId(departmentId)));
    }

    /**
     * 创建教师
     * POST /api/teachers?departmentId=1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Teacher>> create(
            @RequestBody Teacher teacher,
            @RequestParam(required = false) Long departmentId) {
        Teacher created = teacherService.create(teacher, departmentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("教师创建成功", created));
    }

    /**
     * 更新教师信息
     * PUT /api/teachers/{id}?departmentId=1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> update(
            @PathVariable Long id,
            @RequestBody Teacher teacher,
            @RequestParam(required = false) Long departmentId) {
        Teacher updated = teacherService.update(id, teacher, departmentId);
        return ResponseEntity.ok(ApiResponse.success("教师信息更新成功", updated));
    }

    /**
     * 删除教师
     * DELETE /api/teachers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("教师删除成功"));
    }

    /**
     * 按职称统计教师数量
     * GET /api/teachers/statistics/title
     */
    @GetMapping("/statistics/title")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countByTitle() {
        return ResponseEntity.ok(ApiResponse.success(teacherService.countByTitle()));
    }

    /**
     * 获取所有教师姓名列表
     * GET /api/teachers/names
     */
    @GetMapping("/names")
    public ResponseEntity<ApiResponse<List<String>>> getAllNames() {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getAllTeacherNames()));
    }
}
