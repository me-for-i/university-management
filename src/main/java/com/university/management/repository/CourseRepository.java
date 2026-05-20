package com.university.management.repository;

import com.university.management.entity.Course;
import com.university.management.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 课程数据访问层接口
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * 根据课程编号查找
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * 根据课程状态查找
     */
    List<Course> findByStatus(CourseStatus status);

    /**
     * 根据教师ID查找其教授的所有课程
     */
    List<Course> findByTeacherId(Long teacherId);

    /**
     * 根据部门ID查找课程
     */
    List<Course> findByDepartmentId(Long departmentId);

    /**
     * 根据课程名称模糊查找
     */
    List<Course> findByNameContaining(String name);

    /**
     * 判断课程编号是否存在
     */
    boolean existsByCourseCode(String courseCode);
}
