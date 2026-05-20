package com.university.management.service;

import com.university.management.entity.Department;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门服务层
 */
@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("部门", "id", id));
    }

    @Transactional(readOnly = true)
    public Department findByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("部门", "编码", code));
    }

    @Transactional
    public Department create(Department department) {
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new IllegalArgumentException("部门编码已存在: " + department.getCode());
        }
        logger.info("创建部门: {}", department.getName());
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(Long id, Department updated) {
        Department existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setHeadName(updated.getHeadName());
        return departmentRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Department department = findById(id);
        logger.info("删除部门: {}", department.getName());
        departmentRepository.delete(department);
    }
}
