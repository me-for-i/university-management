package com.university.management.repository;

import com.university.management.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 成绩数据访问层接口
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 根据学生ID查找所有成绩
     */
    List<Grade> findByStudentId(Long studentId);

    /**
     * 根据课程ID查找所有成绩
     */
    List<Grade> findByCourseId(Long courseId);

    /**
     * 根据学期查找成绩
     */
    List<Grade> findBySemester(String semester);

    /**
     * 查找某学生某课程的成绩
     */
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * JPQL 查询：计算某课程的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.course.id = :courseId")
    Double getAverageScoreByCourseId(@Param("courseId") Long courseId);

    /**
     * JPQL 查询：查找某学生在某学期的所有成绩
     */
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester")
    List<Grade> findByStudentIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);
}
