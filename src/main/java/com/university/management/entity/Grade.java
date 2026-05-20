package com.university.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩实体类
 * 演示：复合关联关系（学生 + 课程）、BigDecimal精度控制
 */
@Entity
@Table(name = "grades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"department"})
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({"teacher", "department"})
    private Course course;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @Column(length = 200)
    private String remark;

    public Grade() {}

    public Grade(Long id, Student student, Course course, BigDecimal score,
                 String semester, String remark) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.score = score;
        this.semester = semester;
        this.remark = remark;
    }

    @PrePersist
    protected void onCreate() {
        this.recordedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    @Override
    public String toString() {
        return "Grade{id=" + id + ", score=" + score + ", semester='" + semester + "'}";
    }
}
