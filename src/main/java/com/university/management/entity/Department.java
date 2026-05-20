package com.university.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门实体类
 * 演示：一对多关联关系
 */
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(length = 500)
    private String description;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @Column(name = "head_name", length = 50)
    private String headName;

    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"department", "courses"})
    private List<Teacher> teachers = new ArrayList<>();

    public Department() {}

    public Department(Long id, String name, String code, String description,
                      LocalDate establishedDate, String headName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.establishedDate = establishedDate;
        this.headName = headName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getEstablishedDate() { return establishedDate; }
    public void setEstablishedDate(LocalDate establishedDate) { this.establishedDate = establishedDate; }
    public String getHeadName() { return headName; }
    public void setHeadName(String headName) { this.headName = headName; }
    public List<Teacher> getTeachers() { return teachers; }
    public void setTeachers(List<Teacher> teachers) { this.teachers = teachers; }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "', code='" + code + "'}";
    }
}
