package com.university.management.controller;

import com.university.management.dto.ApiResponse;
import com.university.management.entity.Grade;
import com.university.management.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成绩管理控制器
 */
@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    /**
     * 获取所有成绩
     * GET /api/grades
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Grade>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(gradeService.findAll()));
    }

    /**
     * 根据ID获取成绩
     * GET /api/grades/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Grade>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.findById(id)));
    }

    /**
     * 查询某学生的所有成绩
     * GET /api/grades/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Grade>>> findByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.findByStudentId(studentId)));
    }

    /**
     * 查询某课程的所有成绩
     * GET /api/grades/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Grade>>> findByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.findByCourseId(courseId)));
    }

    /**
     * 录入成绩
     * POST /api/grades?studentId=1&courseId=1&score=95.5&semester=2024-1&remark=优秀
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Grade>> create(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam BigDecimal score,
            @RequestParam String semester,
            @RequestParam(required = false) String remark) {
        Grade created = gradeService.create(studentId, courseId, score, semester, remark);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("成绩录入成功", created));
    }

    /**
     * 更新成绩
     * PUT /api/grades/{id}?score=88.5&remark=修改后的备注
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Grade>> update(
            @PathVariable Long id,
            @RequestParam BigDecimal score,
            @RequestParam(required = false) String remark) {
        Grade updated = gradeService.update(id, score, remark);
        return ResponseEntity.ok(ApiResponse.success("成绩更新成功", updated));
    }

    /**
     * 删除成绩
     * DELETE /api/grades/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("成绩删除成功"));
    }

    /**
     * 获取某学生的平均分
     * GET /api/grades/student/{studentId}/average
     */
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<ApiResponse<BigDecimal>> getStudentAverage(@PathVariable Long studentId) {
        BigDecimal average = gradeService.getStudentAverageScore(studentId);
        return ResponseEntity.ok(ApiResponse.success("查询成功", average));
    }

    /**
     * 获取某课程的成绩统计
     * GET /api/grades/course/{courseId}/statistics
     */
    @GetMapping("/course/{courseId}/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCourseStatistics(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getCourseStatistics(courseId)));
    }

    /**
     * 获取学生某学期的成绩单
     * GET /api/grades/student/{studentId}/transcript?semester=2024-1
     */
    @GetMapping("/student/{studentId}/transcript")
    public ResponseEntity<ApiResponse<List<Grade>>> getTranscript(
            @PathVariable Long studentId,
            @RequestParam String semester) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStudentTranscript(studentId, semester)));
    }
}
