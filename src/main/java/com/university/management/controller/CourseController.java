package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.entity.Course;
import com.university.management.enums.CourseStatus;
import com.university.management.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 课程管理控制器
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * 获取所有课程
     * GET /api/courses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(courseService.findAll()));
    }

    /**
     * 根据ID获取课程
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courseService.findById(id)));
    }

    /**
     * 根据课程编号获取课程
     * GET /api/courses/code/{courseCode}
     */
    @GetMapping("/code/{courseCode}")
    public ResponseEntity<ApiResponse<Course>> findByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(ApiResponse.success(courseService.findByCourseCode(courseCode)));
    }

    /**
     * 根据状态查找课程
     * GET /api/courses/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Course>>> findByStatus(@PathVariable CourseStatus status) {
        return ResponseEntity.ok(ApiResponse.success(courseService.findByStatus(status)));
    }

    /**
     * 根据课程名称模糊搜索
     * GET /api/courses/search?name=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Course>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(courseService.searchByName(name)));
    }

    /**
     * 根据教师ID查找课程
     * GET /api/courses/teacher/{teacherId}
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<Course>>> findByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.findByTeacherId(teacherId)));
    }

    /**
     * 创建课程
     * POST /api/courses?teacherId=1&departmentId=1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> create(
            @RequestBody Course course,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long departmentId) {
        Course created = courseService.create(course, teacherId, departmentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("课程创建成功", created));
    }

    /**
     * 更新课程
     * PUT /api/courses/{id}?teacherId=1&departmentId=1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> update(
            @PathVariable Long id,
            @RequestBody Course course,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long departmentId) {
        Course updated = courseService.update(id, course, teacherId, departmentId);
        return ResponseEntity.ok(ApiResponse.success("课程更新成功", updated));
    }

    /**
     * 更新课程状态
     * PATCH /api/courses/{id}/status?status=ACTIVE
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Course>> updateStatus(
            @PathVariable Long id,
            @RequestParam CourseStatus status) {
        Course updated = courseService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("课程状态更新成功", updated));
    }

    /**
     * 删除课程
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("课程删除成功"));
    }

    /**
     * 按状态分组统计课程
     * GET /api/courses/statistics/status
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<ApiResponse<Map<CourseStatus, List<Course>>>> groupByStatus() {
        return ResponseEntity.ok(ApiResponse.success(courseService.groupByStatus()));
    }

    /**
     * 获取总学分
     * GET /api/courses/statistics/total-credits
     */
    @GetMapping("/statistics/total-credits")
    public ResponseEntity<ApiResponse<Integer>> getTotalCredits() {
        return ResponseEntity.ok(ApiResponse.success(courseService.getTotalCredits()));
    }
}
