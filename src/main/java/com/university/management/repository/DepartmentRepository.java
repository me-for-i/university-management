package com.university.management.repository;

import com.university.management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 部门数据访问层接口
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 根据部门编码查找
     */
    Optional<Department> findByCode(String code);

    /**
     * 根据部门名称查找
     */
    Optional<Department> findByName(String name);

    /**
     * 判断部门编码是否存在
     */
    boolean existsByCode(String code);
}
