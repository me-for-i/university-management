package com.university.management.service;

import com.university.management.entity.Course;
import com.university.management.entity.Grade;
import com.university.management.entity.Student;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.GradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成绩服务层
 * 演示：BigDecimal 精确计算、Stream 统计操作、复杂集合处理
 */
@Service
public class GradeService {

    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);
    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    public GradeService(GradeRepository gradeRepository, StudentService studentService,
                        CourseService courseService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Transactional(readOnly = true)
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Grade findById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("成绩", "id", id));
    }

    /**
     * 查询某学生的所有成绩
     */
    @Transactional(readOnly = true)
    public List<Grade> findByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    /**
     * 查询某课程的所有成绩
     */
    @Transactional(readOnly = true)
    public List<Grade> findByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    /**
     * 录入成绩
     */
    @Transactional
    public Grade create(Long studentId, Long courseId, BigDecimal score, String semester, String remark) {
        // 验证学生和课程是否存在
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        // 验证分数范围
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("分数必须在0-100之间，当前: " + score);
        }

        // 检查是否已存在成绩记录
        Optional<Grade> existing = gradeRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("学生[%s]的课程[%s]成绩已存在", student.getName(), course.getName()));
        }

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(score);
        grade.setSemester(semester);
        grade.setRemark(remark);

        logger.info("录入成绩: 学生[{}] 课程[{}] 分数[{}]",
                student.getName(), course.getName(), score);
        return gradeRepository.save(grade);
    }

    /**
     * 更新成绩
     */
    @Transactional
    public Grade update(Long id, BigDecimal newScore, String remark) {
        Grade grade = findById(id);

        if (newScore.compareTo(BigDecimal.ZERO) < 0 || newScore.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("分数必须在0-100之间");
        }

        grade.setScore(newScore);
        Optional.ofNullable(remark).ifPresent(grade::setRemark);
        return gradeRepository.save(grade);
    }

    /**
     * 删除成绩
     */
    @Transactional
    public void delete(Long id) {
        Grade grade = findById(id);
        gradeRepository.delete(grade);
    }

    /**
     * 计算某学生的平均分
     * 演示：BigDecimal 精确计算 + Stream reduce
     *
     * @param studentId 学生ID
     * @return 平均分（保留两位小数）
     */
    @Transactional(readOnly = true)
    public BigDecimal getStudentAverageScore(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        if (grades.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 使用 Stream reduce 累加所有分数
        BigDecimal total = grades.stream()
                .map(Grade::getScore)                              // 提取分数
                .reduce(BigDecimal.ZERO, BigDecimal::add);        // 累加

        // BigDecimal 除法，保留2位小数，四舍五入
        return total.divide(
                BigDecimal.valueOf(grades.size()),
                2,
                RoundingMode.HALF_UP
        );
    }

    /**
     * 获取某课程的成绩统计信息
     * 演示：DoubleSummaryStatistics 统计操作
     *
     * @param courseId 课程ID
     * @return 包含最高分、最低分、平均分等统计信息的 Map
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getCourseStatistics(Long courseId) {
        List<Grade> grades = gradeRepository.findByCourseId(courseId);

        if (grades.isEmpty()) {
            return Collections.emptyMap();
        }

        // 使用 DoubleSummaryStatistics 进行统计
        DoubleSummaryStatistics stats = grades.stream()
                .mapToDouble(g -> g.getScore().doubleValue())     // 转为 DoubleStream
                .summaryStatistics();                              // 获取统计信息

        // 使用 LinkedHashMap 保持插入顺序
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("课程ID", courseId);
        result.put("参加人数", stats.getCount());
        result.put("最高分", stats.getMax());
        result.put("最低分", stats.getMin());
        result.put("平均分", String.format("%.2f", stats.getAverage()));

        // 计算及格人数（>=60分）
        long passCount = grades.stream()
                .filter(g -> g.getScore().compareTo(new BigDecimal("60")) >= 0)
                .count();
        result.put("及格人数", passCount);
        result.put("及格率", String.format("%.1f%%", (double) passCount / grades.size() * 100));

        return result;
    }

    /**
     * 获取某学生某学期的成绩单
     * 演示：Stream 排序 + 自定义比较器
     */
    @Transactional(readOnly = true)
    public List<Grade> getStudentTranscript(Long studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester)
                .stream()
                .sorted(Comparator.comparing(Grade::getScore).reversed())  // 按分数降序排列
                .collect(Collectors.toList());
    }
}
