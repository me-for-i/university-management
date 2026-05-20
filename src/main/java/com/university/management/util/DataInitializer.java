package com.university.management.util;

import com.university.management.entity.*;
import com.university.management.enums.CourseStatus;
import com.university.management.enums.Gender;
import com.university.management.enums.UserRole;
import com.university.management.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化器
 * 实现 CommandLineRunner 接口，在 Spring Boot 启动后自动执行
 * 用于插入初始测试数据
 *
 * 演示：CommandLineRunner 接口、Arrays.asList、setter 构建对象、批量操作
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;

    public DataInitializer(UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           TeacherRepository teacherRepository,
                           StudentRepository studentRepository,
                           CourseRepository courseRepository,
                           GradeRepository gradeRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("数据库已有数据，跳过初始化");
            return;
        }
        logger.info("开始初始化测试数据...");
        initUsers();
        List<Department> departments = initDepartments();
        List<Teacher> teachers = initTeachers(departments);
        List<Student> students = initStudents(departments);
        List<Course> courses = initCourses(teachers, departments);
        initGrades(students, courses);
        logger.info("测试数据初始化完成！");
    }

    /**
     * 创建用户的辅助方法
     * 演示：封装对象创建逻辑，减少重复代码
     */
    private User createUser(String username, String password, String realName,
                            UserRole role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setRole(role);
        user.setEmail(email);
        user.setEnabled(true);
        return user;
    }

    private void initUsers() {
        // 使用 Arrays.asList 创建不可变大小的列表
        List<User> users = Arrays.asList(
                createUser("admin", "admin123", "系统管理员", UserRole.ADMIN, "admin@university.edu.cn"),
                createUser("teacher1", "teacher123", "张教授", UserRole.TEACHER, "zhang@university.edu.cn"),
                createUser("student1", "student123", "李明", UserRole.STUDENT, "liming@university.edu.cn")
        );
        userRepository.saveAll(users);
        logger.info("初始化了 {} 个用户", users.size());
    }

    private Department createDepartment(String name, String code, String description,
                                        LocalDate establishedDate, String headName) {
        Department dept = new Department();
        dept.setName(name);
        dept.setCode(code);
        dept.setDescription(description);
        dept.setEstablishedDate(establishedDate);
        dept.setHeadName(headName);
        return dept;
    }

    private List<Department> initDepartments() {
        List<Department> departments = Arrays.asList(
                createDepartment("计算机科学与技术学院", "CS",
                        "培养计算机科学与技术领域的高级专门人才",
                        LocalDate.of(1985, 9, 1), "王院长"),
                createDepartment("数学与统计学院", "MATH",
                        "从事数学和统计学教学与研究",
                        LocalDate.of(1952, 9, 1), "刘院长"),
                createDepartment("外国语学院", "FL",
                        "培养外语类专门人才",
                        LocalDate.of(1978, 9, 1), "陈院长"),
                createDepartment("经济管理学院", "EM",
                        "培养经济学和管理学领域人才",
                        LocalDate.of(1990, 3, 1), "赵院长")
        );
        departments = departmentRepository.saveAll(departments);
        logger.info("初始化了 {} 个部门", departments.size());
        return departments;
    }

    private Teacher createTeacher(String employeeId, String name, Gender gender,
                                  LocalDate birthDate, String phone, String email,
                                  String title, LocalDate hireDate, Department dept) {
        Teacher t = new Teacher();
        t.setEmployeeId(employeeId);
        t.setName(name);
        t.setGender(gender);
        t.setBirthDate(birthDate);
        t.setPhone(phone);
        t.setEmail(email);
        t.setTitle(title);
        t.setHireDate(hireDate);
        t.setDepartment(dept);
        return t;
    }

    private List<Teacher> initTeachers(List<Department> departments) {
        Department csDept = departments.get(0);
        Department mathDept = departments.get(1);

        List<Teacher> teachers = Arrays.asList(
                createTeacher("T2020001", "张伟", Gender.MALE,
                        LocalDate.of(1975, 3, 15), "13800138001",
                        "zhangwei@university.edu.cn", "教授",
                        LocalDate.of(2000, 7, 1), csDept),
                createTeacher("T2020002", "李芳", Gender.FEMALE,
                        LocalDate.of(1980, 8, 22), "13800138002",
                        "lifang@university.edu.cn", "副教授",
                        LocalDate.of(2005, 9, 1), csDept),
                createTeacher("T2020003", "王强", Gender.MALE,
                        LocalDate.of(1982, 12, 5), "13800138003",
                        "wangqiang@university.edu.cn", "讲师",
                        LocalDate.of(2010, 3, 1), mathDept),
                createTeacher("T2020004", "赵丽", Gender.FEMALE,
                        LocalDate.of(1985, 6, 18), "13800138004",
                        "zhaoli@university.edu.cn", "助教",
                        LocalDate.of(2015, 7, 1), mathDept)
        );
        teachers = teacherRepository.saveAll(teachers);
        logger.info("初始化了 {} 名教师", teachers.size());
        return teachers;
    }

    private Student createStudent(String studentId, String name, Gender gender,
                                  LocalDate birthDate, String phone, String email,
                                  int grade, String className, LocalDate enrollmentDate,
                                  Department dept) {
        Student s = new Student();
        s.setStudentId(studentId);
        s.setName(name);
        s.setGender(gender);
        s.setBirthDate(birthDate);
        s.setPhone(phone);
        s.setEmail(email);
        s.setGrade(grade);
        s.setClassName(className);
        s.setEnrollmentDate(enrollmentDate);
        s.setDepartment(dept);
        return s;
    }

    private List<Student> initStudents(List<Department> departments) {
        Department csDept = departments.get(0);
        Department mathDept = departments.get(1);

        List<Student> students = Arrays.asList(
                createStudent("S2022001", "李明", Gender.MALE,
                        LocalDate.of(2003, 5, 10), "15900159001",
                        "liming@stu.university.edu.cn", 2022,
                        "计算机1班", LocalDate.of(2022, 9, 1), csDept),
                createStudent("S2022002", "王芳", Gender.FEMALE,
                        LocalDate.of(2003, 11, 25), "15900159002",
                        "wangfang@stu.university.edu.cn", 2022,
                        "计算机1班", LocalDate.of(2022, 9, 1), csDept),
                createStudent("S2023001", "刘洋", Gender.MALE,
                        LocalDate.of(2004, 2, 14), "15900159003",
                        "liuyang@stu.university.edu.cn", 2023,
                        "数学1班", LocalDate.of(2023, 9, 1), mathDept),
                createStudent("S2023002", "张雪", Gender.FEMALE,
                        LocalDate.of(2004, 7, 30), "15900159004",
                        "zhangxue@stu.university.edu.cn", 2023,
                        "计算机2班", LocalDate.of(2023, 9, 1), csDept),
                createStudent("S2024001", "陈磊", Gender.MALE,
                        LocalDate.of(2005, 4, 8), "15900159005",
                        "chenlei@stu.university.edu.cn", 2024,
                        "数学2班", LocalDate.of(2024, 9, 1), mathDept)
        );
        students = studentRepository.saveAll(students);
        logger.info("初始化了 {} 名学生", students.size());
        return students;
    }

    private Course createCourse(String courseCode, String name, String description,
                                int credits, int totalHours, int maxStudents,
                                CourseStatus status, Teacher teacher, Department dept) {
        Course c = new Course();
        c.setCourseCode(courseCode);
        c.setName(name);
        c.setDescription(description);
        c.setCredits(credits);
        c.setTotalHours(totalHours);
        c.setMaxStudents(maxStudents);
        c.setStatus(status);
        c.setTeacher(teacher);
        c.setDepartment(dept);
        return c;
    }

    private List<Course> initCourses(List<Teacher> teachers, List<Department> departments) {
        Teacher zhangWei = teachers.get(0);
        Teacher liFang = teachers.get(1);
        Teacher wangQiang = teachers.get(2);
        Department csDept = departments.get(0);
        Department mathDept = departments.get(1);

        List<Course> courses = Arrays.asList(
                createCourse("CS101", "Java程序设计",
                        "学习Java编程语言基础和面向对象编程",
                        4, 64, 60, CourseStatus.ACTIVE, zhangWei, csDept),
                createCourse("CS201", "数据结构与算法",
                        "学习常用数据结构和算法设计",
                        4, 64, 60, CourseStatus.ACTIVE, liFang, csDept),
                createCourse("MATH101", "高等数学",
                        "微积分、线性代数、概率论基础",
                        5, 80, 100, CourseStatus.ACTIVE, wangQiang, mathDept),
                createCourse("CS301", "Spring Boot实战",
                        "学习Spring Boot框架开发企业级应用",
                        3, 48, 40, CourseStatus.INACTIVE, zhangWei, csDept)
        );
        courses = courseRepository.saveAll(courses);
        logger.info("初始化了 {} 门课程", courses.size());
        return courses;
    }

    private Grade createGrade(Student student, Course course, String score,
                              String semester, String remark) {
        Grade g = new Grade();
        g.setStudent(student);
        g.setCourse(course);
        g.setScore(new BigDecimal(score));
        g.setSemester(semester);
        g.setRemark(remark);
        return g;
    }

    private void initGrades(List<Student> students, List<Course> courses) {
        Student liMing = students.get(0);
        Student wangFang = students.get(1);
        Student liuYang = students.get(2);
        Course java = courses.get(0);
        Course dataStructure = courses.get(1);
        Course math = courses.get(2);

        List<Grade> grades = Arrays.asList(
                createGrade(liMing, java, "92.5", "2024-1", "表现优秀"),
                createGrade(liMing, dataStructure, "85.0", "2024-1", "良好"),
                createGrade(liMing, math, "78.5", "2024-1", null),
                createGrade(wangFang, java, "96.0", "2024-1", "非常优秀"),
                createGrade(wangFang, math, "88.0", "2024-1", null),
                createGrade(liuYang, math, "72.0", "2024-1", "需要加强练习")
        );
        gradeRepository.saveAll(grades);
        logger.info("初始化了 {} 条成绩记录", grades.size());
    }
}
