package com.university.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.university.management.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
