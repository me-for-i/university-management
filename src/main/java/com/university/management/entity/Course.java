package com.university.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.university.management.enums.CourseStatus;
import jakarta.persistence.*;

/**
 * 课程实体类
 * 演示：多对一关联、枚举状态管理
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 课程编号（唯一标识） */
    @Column(name = "course_code", nullable = false, unique = true, length = 20)
    private String courseCode;

    /** 课程名称 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 课程描述 */
    @Column(length = 500)
    private String description;

    /** 学分 */
    @Column(nullable = false)
    private Integer credits;

    /** 课时数 */
    @Column(name = "total_hours")
    private Integer totalHours;

    /** 最大选课人数 */
    @Column(name = "max_students")
    private Integer maxStudents;

    /** 课程状态（枚举类型） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.INACTIVE;

    /** 多对一：每门课程由一名教师主讲 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({"courses", "department"})
    private Teacher teacher;

    /** 多对一：课程归属某个部门 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"teachers"})
    private Department department;

    // ========== 构造方法 ==========

    public Course() {
    }

    public Course(Long id, String courseCode, String name, String description,
                  Integer credits, Integer totalHours, Integer maxStudents,
                  CourseStatus status, Teacher teacher, Department department) {
        this.id = id;
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.totalHours = totalHours;
        this.maxStudents = maxStudents;
        this.status = status;
        this.teacher = teacher;
        this.department = department;
    }

    // ========== Getter / Setter ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", courseCode='" + courseCode
                + "', name='" + name + "', credits=" + credits + "}";
    }
}
