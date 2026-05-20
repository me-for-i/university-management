package com.university.management.service;

import com.university.management.entity.Department;
import com.university.management.entity.Student;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生服务层
 * 演示：Stream API 的各种高级操作、集合分组、排序、过滤
 */
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final DepartmentService departmentService;

    public StudentService(StudentRepository studentRepository, DepartmentService departmentService) {
        this.studentRepository = studentRepository;
        this.departmentService = departmentService;
    }

    /**
     * 获取所有学生
     */
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * 根据ID查找学生
     */
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("学生", "id", id));
    }

    /**
     * 根据学号查找学生
     */
    @Transactional(readOnly = true)
    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生", "学号", studentId));
    }

    /**
     * 根据姓名模糊搜索
     */
    @Transactional(readOnly = true)
    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    /**
     * 根据年级查找学生
     */
    @Transactional(readOnly = true)
    public List<Student> findByGrade(Integer grade) {
        return studentRepository.findByGrade(grade);
    }

    /**
     * 创建新学生
     */
    @Transactional
    public Student create(Student student, Long departmentId) {
        // 校验学号唯一性
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }

        // 关联部门（学院）
        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            student.setDepartment(dept);
        }

        logger.info("创建学生: {} (学号: {})", student.getName(), student.getStudentId());
        return studentRepository.save(student);
    }

    /**
     * 更新学生信息
     */
    @Transactional
    public Student update(Long id, Student updated, Long departmentId) {
        Student existing = findById(id);

        Optional.ofNullable(updated.getName()).ifPresent(existing::setName);
        Optional.ofNullable(updated.getGender()).ifPresent(existing::setGender);
        Optional.ofNullable(updated.getPhone()).ifPresent(existing::setPhone);
        Optional.ofNullable(updated.getEmail()).ifPresent(existing::setEmail);
        Optional.ofNullable(updated.getGrade()).ifPresent(existing::setGrade);
        Optional.ofNullable(updated.getClassName()).ifPresent(existing::setClassName);
        Optional.ofNullable(updated.getBirthDate()).ifPresent(existing::setBirthDate);

        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            existing.setDepartment(dept);
        }

        return studentRepository.save(existing);
    }

    /**
     * 删除学生
     */
    @Transactional
    public void delete(Long id) {
        Student student = findById(id);
        logger.info("删除学生: {} (学号: {})", student.getName(), student.getStudentId());
        studentRepository.delete(student);
    }

    /**
     * 按年级分组统计学生
     * 演示：Stream groupingBy + TreeMap（有序Map）
     *
     * @return TreeMap<年级, 学生列表>（按年级排序）
     */
    @Transactional(readOnly = true)
    public Map<Integer, List<Student>> groupByGrade() {
        return studentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,                      // 按年级分组
                        TreeMap::new,                           // 使用 TreeMap 保证年级有序
                        Collectors.toList()                     // 收集为列表
                ));
    }

    /**
     * 获取各年级学生人数统计
     * 演示：Stream groupingBy + counting + 排序输出
     *
     * @return Map<年级, 人数>
     */
    @Transactional(readOnly = true)
    public Map<Integer, Long> countByGrade() {
        return studentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * 获取某年级的所有学生姓名（按字母排序）
     * 演示：filter + map + sorted + collect 链式操作
     *
     * @param grade 年级
     * @return 排序后的学生姓名列表
     */
    @Transactional(readOnly = true)
    public List<String> getStudentNamesByGrade(int grade) {
        return studentRepository.findAll()
                .stream()
                .filter(s -> s.getGrade() != null && s.getGrade() == grade)  // 过滤指定年级
                .map(Student::getName)                                         // 提取姓名
                .sorted(Comparator.naturalOrder())                             // 自然排序
                .collect(Collectors.toUnmodifiableList());                     // 收集为不可变列表
    }

    /**
     * 使用 Set 去重：获取所有不重复的年级
     * 演示：Stream + Collectors.toSet
     *
     * @return 年级集合（不重复）
     */
    @Transactional(readOnly = true)
    public Set<Integer> getAllGrades() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getGrade)                  // 提取年级字段
                .filter(Objects::nonNull)                // 过滤 null 值
                .collect(Collectors.toCollection(TreeSet::new));  // 收集为有序 Set
    }
}
