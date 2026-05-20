package com.university.management.service;

import com.university.management.entity.Department;
import com.university.management.entity.Teacher;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 教师服务层
 * 演示：Stream API、Lambda 表达式、集合操作、泛型方法
 */
@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepository teacherRepository;
    private final DepartmentService departmentService;

    public TeacherService(TeacherRepository teacherRepository, DepartmentService departmentService) {
        this.teacherRepository = teacherRepository;
        this.departmentService = departmentService;
    }

    /**
     * 获取所有教师列表
     */
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    /**
     * 根据ID查找教师
     */
    @Transactional(readOnly = true)
    public Teacher findById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("教师", "id", id));
    }

    /**
     * 根据工号查找教师
     */
    @Transactional(readOnly = true)
    public Teacher findByEmployeeId(String employeeId) {
        return teacherRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("教师", "工号", employeeId));
    }

    /**
     * 根据姓名模糊搜索教师
     */
    @Transactional(readOnly = true)
    public List<Teacher> searchByName(String name) {
        return teacherRepository.findByNameContaining(name);
    }

    /**
     * 根据部门ID查找教师列表
     */
    @Transactional(readOnly = true)
    public List<Teacher> findByDepartmentId(Long departmentId) {
        return teacherRepository.findByDepartmentId(departmentId);
    }

    /**
     * 创建新教师
     */
    @Transactional
    public Teacher create(Teacher teacher, Long departmentId) {
        // 校验工号唯一性
        if (teacherRepository.existsByEmployeeId(teacher.getEmployeeId())) {
            throw new IllegalArgumentException("工号已存在: " + teacher.getEmployeeId());
        }

        // 关联部门
        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            teacher.setDepartment(dept);
        }

        logger.info("创建教师: {} (工号: {})", teacher.getName(), teacher.getEmployeeId());
        return teacherRepository.save(teacher);
    }

    /**
     * 更新教师信息
     */
    @Transactional
    public Teacher update(Long id, Teacher updated, Long departmentId) {
        Teacher existing = findById(id);

        // 更新基本字段
        Optional.ofNullable(updated.getName()).ifPresent(existing::setName);
        Optional.ofNullable(updated.getGender()).ifPresent(existing::setGender);
        Optional.ofNullable(updated.getPhone()).ifPresent(existing::setPhone);
        Optional.ofNullable(updated.getEmail()).ifPresent(existing::setEmail);
        Optional.ofNullable(updated.getTitle()).ifPresent(existing::setTitle);
        Optional.ofNullable(updated.getBirthDate()).ifPresent(existing::setBirthDate);

        // 更新部门关联
        if (departmentId != null) {
            Department dept = departmentService.findById(departmentId);
            existing.setDepartment(dept);
        }

        return teacherRepository.save(existing);
    }

    /**
     * 删除教师
     */
    @Transactional
    public void delete(Long id) {
        Teacher teacher = findById(id);
        logger.info("删除教师: {} (工号: {})", teacher.getName(), teacher.getEmployeeId());
        teacherRepository.delete(teacher);
    }

    /**
     * 按职称分组统计教师
     * 演示：Stream API 的 groupingBy + counting
     *
     * @return Map<职称, 人数>
     */
    @Transactional(readOnly = true)
    public Map<String, Long> countByTitle() {
        List<Teacher> allTeachers = teacherRepository.findAll();

        // Stream API: 按职称分组并计数
        return allTeachers.stream()
                .filter(t -> t.getTitle() != null)                 // 过滤掉无职称的教师
                .collect(Collectors.groupingBy(                    // 分组操作
                        Teacher::getTitle,                         // 方法引用：按职称分组
                        Collectors.counting()                     // 下游收集器：计数
                ));
    }

    /**
     * 获取教师名单（仅返回姓名列表）
     * 演示：Stream API 的 map + toList
     *
     * @return 教师姓名列表
     */
    @Transactional(readOnly = true)
    public List<String> getAllTeacherNames() {
        return teacherRepository.findAll()
                .stream()                                          // 转为 Stream
                .map(Teacher::getName)                             // 方法引用：提取姓名字段
                .sorted()                                          // 自然排序（按字母/拼音）
                .collect(Collectors.toList());                     // 收集为 List
    }

    /**
     * 将教师列表转换为 Map（以工号为 Key）
     * 演示：Stream + Collectors.toMap + Function.identity
     *
     * @return Map<工号, 教师对象>
     */
    @Transactional(readOnly = true)
    public Map<String, Teacher> getTeacherMapByEmployeeId() {
        return teacherRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Teacher::getEmployeeId,         // Key 映射：工号
                        Function.identity()             // Value 映射：教师对象本身
                ));
    }
}
