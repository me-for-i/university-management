package com.university.management.repository;

import com.university.management.entity.User;
import com.university.management.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层接口
 * 继承 JpaRepository 即可获得基本的 CRUD 操作能力
 * 泛型参数：<实体类型, 主键类型>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * Spring Data JPA 会根据方法名自动生成查询语句
     *
     * @param username 用户名
     * @return Optional 包装的用户对象（避免空指针）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据角色查找所有用户
     * @param role 用户角色枚举
     * @return 用户列表
     */
    List<User> findByRole(UserRole role);

    /**
     * 判断用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 使用 JPQL 自定义查询：根据用户名和密码查找已启用的用户
     * @param username 用户名
     * @param password 密码
     * @return Optional 包装的用户对象
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.enabled = true")
    Optional<User> findByUsernameAndPasswordAndEnabled(
            @Param("username") String username,
            @Param("password") String password
    );

    /**
     * 根据真实姓名模糊查询
     * @param name 姓名关键字
     * @return 用户列表
     */
    List<User> findByRealNameContaining(String name);
}
