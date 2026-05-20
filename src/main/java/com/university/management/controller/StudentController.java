package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.entity.Student;
import com.university.management.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学生管理控制器
 * 演示：RESTful CRUD + 统计分析接口
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 获取所有学生
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(studentService.findAll()));
    }

    /**
     * 根据ID获取学生
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(studentService.findById(id)));
    }

    /**
     * 根据学号获取学生
     * GET /api/students/sid/{studentId}
     */
    @GetMapping("/sid/{studentId}")
    public ResponseEntity<ApiResponse<Student>> findByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.findByStudentId(studentId)));
    }

    /**
     * 根据姓名模糊搜索
     * GET /api/students/search?name=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Student>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(studentService.searchByName(name)));
    }

    /**
     * 根据年级查找
     * GET /api/students/grade/{grade}
     */
    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse<List<Student>>> findByGrade(@PathVariable Integer grade) {
        return ResponseEntity.ok(ApiResponse.success(studentService.findByGrade(grade)));
    }

    /**
     * 创建学生
     * POST /api/students?departmentId=1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> create(
            @RequestBody Student student,
            @RequestParam(required = false) Long departmentId) {
        Student created = studentService.create(student, departmentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("学生创建成功", created));
    }

    /**
     * 更新学生信息
     * PUT /api/students/{id}?departmentId=1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> update(
            @PathVariable Long id,
            @RequestBody Student student,
            @RequestParam(required = false) Long departmentId) {
        Student updated = studentService.update(id, student, departmentId);
        return ResponseEntity.ok(ApiResponse.success("学生信息更新成功", updated));
    }

    /**
     * 删除学生
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("学生删除成功"));
    }

    /**
     * 按年级统计学生人数
     * GET /api/students/statistics/grade
     */
    @GetMapping("/statistics/grade")
    public ResponseEntity<ApiResponse<Map<Integer, Long>>> countByGrade() {
        return ResponseEntity.ok(ApiResponse.success(studentService.countByGrade()));
    }

    /**
     * 获取所有年级列表
     * GET /api/students/grades
     */
    @GetMapping("/grades")
    public ResponseEntity<ApiResponse<Set<Integer>>> getAllGrades() {
        return ResponseEntity.ok(ApiResponse.success(studentService.getAllGrades()));
    }
}
