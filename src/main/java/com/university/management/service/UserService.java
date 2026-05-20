package com.university.management.service;

import com.university.management.dto.LoginRequest;
import com.university.management.dto.LoginResponse;
import com.university.management.entity.User;
import com.university.management.enums.UserRole;
import com.university.management.exception.ResourceNotFoundException;
import com.university.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务层
 * 演示：依赖注入、Optional 使用、事务管理
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // 通过构造方法注入（推荐方式，替代 @Autowired 字段注入）
    private final UserRepository userRepository;

    /**
     * 构造方法注入 Repository
     * Spring 会自动注入匹配的 Bean（单构造方法时可省略 @Autowired）
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 用户登录验证
     * 演示：Optional 的链式操作
     *
     * @param request 登录请求 DTO
     * @return 登录响应
     * @throws IllegalArgumentException 如果用户名或密码错误
     */
    public LoginResponse login(LoginRequest request) {
        logger.info("用户尝试登录: {}", request.username());

        // 使用 Optional 避免空指针，链式调用 map 转换结果
        return userRepository.findByUsernameAndPasswordAndEnabled(
                        request.username(), request.password())
                .map(user -> {
                    // Lambda 表达式：将 User 实体转换为 LoginResponse
                    logger.info("登录成功: {}", user.getUsername());
                    return new LoginResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getRealName(),
                            user.getRole(),
                            "登录成功"
                    );
                })
                .orElseThrow(() -> {
                    // 登录失败时抛出异常
                    logger.warn("登录失败，用户名或密码错误: {}", request.username());
                    return new IllegalArgumentException("用户名或密码错误，或账号已被禁用");
                });
    }

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据ID查找用户
     * 演示：Optional + orElseThrow
     */
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));
    }

    /**
     * 根据角色查找用户
     */
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * 创建新用户
     * @Transactional 确保操作的原子性
     */
    @Transactional
    public User createUser(User user) {
        // 校验用户名唯一性
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在: " + user.getUsername());
        }
        logger.info("创建新用户: {}", user.getUsername());
        return userRepository.save(user);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = findById(id);

        // 使用 Optional.ofNullable 安全地更新字段（只更新非空字段）
        Optional.ofNullable(updatedUser.getRealName()).ifPresent(existingUser::setRealName);
        Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(updatedUser.getRole()).ifPresent(existingUser::setRole);
        Optional.ofNullable(updatedUser.getEnabled()).ifPresent(existingUser::setEnabled);

        return userRepository.save(existingUser);
    }

    /**
     * 禁用用户账号
     */
    @Transactional
    public void disableUser(Long id) {
        User user = findById(id);
        user.setEnabled(false);
        userRepository.save(user);
        logger.info("用户已被禁用: {}", user.getUsername());
    }
}
