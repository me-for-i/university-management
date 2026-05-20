package com.university.management.repository;

import com.university.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层接口
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 根据学号查找学生
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * 根据姓名模糊查找
     */
    List<Student> findByNameContaining(String name);

    /**
     * 根据年级查找
     */
    List<Student> findByGrade(Integer grade);

    /**
     * 根据部门（学院）ID查找
     */
    List<Student> findByDepartmentId(Long departmentId);

    /**
     * 判断学号是否存在
     */
    boolean existsByStudentId(String studentId);

    /**
     * 原生 SQL 查询示例：按年级统计学生人数
     */
    @Query(value = "SELECT grade, COUNT(*) as count FROM students GROUP BY grade", nativeQuery = true)
    List<Object[]> countStudentsByGrade();

    /**
     * JPQL 查询：根据部门和年级查找学生
     */
    @Query("SELECT s FROM Student s WHERE s.department.id = :deptId AND s.grade = :grade")
    List<Student> findByDepartmentIdAndGrade(@Param("deptId") Long departmentId, @Param("grade") Integer grade);
}
