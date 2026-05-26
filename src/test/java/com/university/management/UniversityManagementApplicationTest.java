package com.university.management;

import com.university.management.entity.Teacher;
import com.university.management.entity.User;
import com.university.management.mapper.TeacherMapper;
import com.university.management.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UniversityManagementApplicationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void testMapper() {
        List<Teacher> teacherList = teacherMapper.selectList( null);
        teacherList.forEach(System.out::println);
    }
}
