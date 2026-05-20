package com.university.management.service;

import com.university.management.entity.Course;
import com.university.management.entity.Department;
import com.university.management.entity.Teacher;
import com.university.management.enums.CourseStatus;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 课程服务层
 * 演示：枚举状态管理、Stream 分区操作
 */
@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    public CourseService(CourseRepository courseRepository, TeacherService teacherService,
                         DepartmentService departmentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("课程", "id", id));
    }

    @Transactional(readOnly = true)
    public Course findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("课程", "课程编号", courseCode));
    }

    @Transactional(readOnly = true)
    public List<Course> findByStatus(CourseStatus status) {
        return courseRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Transactional(readOnly = true)
    public List<Course> searchByName(String name) {
        return courseRepository.findByNameContaining(name);
    }

    @Transactional
    public Course create(Course course, Long teacherId, Long departmentId) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("课程编号已存在: " + course.getCourseCode());
        }

        // 关联教师
        if (teacherId != null) {
            Teacher teacher = teacherService.findById(teacherId);
            course.setTeacher(teacher);
        }

        // 关联部门
        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            course.setDepartment(dept);
        }

        logger.info("创建课程: {} (编号: {})", course.getName(), course.getCourseCode());
        return courseRepository.save(course);
    }

    @Transactional
    public Course update(Long id, Course updated, Long teacherId, Long departmentId) {
        Course existing = findById(id);

        Optional.ofNullable(updated.getName()).ifPresent(existing::setName);
        Optional.ofNullable(updated.getDescription()).ifPresent(existing::setDescription);
        Optional.ofNullable(updated.getCredits()).ifPresent(existing::setCredits);
        Optional.ofNullable(updated.getTotalHours()).ifPresent(existing::setTotalHours);
        Optional.ofNullable(updated.getMaxStudents()).ifPresent(existing::setMaxStudents);
        Optional.ofNullable(updated.getStatus()).ifPresent(existing::setStatus);

        if (teacherId != null) {
            Teacher teacher = teacherService.findById(teacherId);
            existing.setTeacher(teacher);
        }
        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            existing.setDepartment(dept);
        }

        return courseRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Course course = findById(id);
        logger.info("删除课程: {} (编号: {})", course.getName(), course.getCourseCode());
        courseRepository.delete(course);
    }

    /**
     * 更新课程状态
     */
    @Transactional
    public Course updateStatus(Long id, CourseStatus newStatus) {
        Course course = findById(id);
        CourseStatus oldStatus = course.getStatus();
        course.setStatus(newStatus);
        logger.info("课程[{}]状态更新: {} -> {}", course.getName(),
                oldStatus.getDescription(), newStatus.getDescription());
        return courseRepository.save(course);
    }

    /**
     * 按状态分组课程
     * 演示：Stream partitioningBy（二分区）和 groupingBy
     *
     * @return Map<课程状态, 课程列表>
     */
    @Transactional(readOnly = true)
    public Map<CourseStatus, List<Course>> groupByStatus() {
        return courseRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Course::getStatus));
    }

    /**
     * 获取总学分
     * 演示：Stream reduce 操作
     *
     * @return 所有课程的总学分
     */
    @Transactional(readOnly = true)
    public int getTotalCredits() {
        return courseRepository.findAll()
                .stream()
                .map(Course::getCredits)                    // 提取学分
                .filter(credits -> credits != null)         // 过滤空值
                .reduce(0, Integer::sum);                   // 累加求和
    }
}
