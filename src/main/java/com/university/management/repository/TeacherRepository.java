package com.university.management.repository;

import com.university.management.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 教师数据访问层接口
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * 根据工号查找教师
     */
    Optional<Teacher> findByEmployeeId(String employeeId);

    /**
     * 根据姓名模糊查找教师
     */
    List<Teacher> findByNameContaining(String name);

    /**
     * 根据部门ID查找教师列表
     */
    List<Teacher> findByDepartmentId(Long departmentId);

    /**
     * 根据职称查找教师
     */
    List<Teacher> findByTitle(String title);

    /**
     * 判断工号是否存在
     */
    boolean existsByEmployeeId(String employeeId);

    /**
     * JPQL 查询：统计某部门的教师数量
     */
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department.id = :deptId")
    long countByDepartmentId(@Param("deptId") Long departmentId);
}
