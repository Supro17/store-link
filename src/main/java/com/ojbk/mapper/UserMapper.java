package com.ojbk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ojbk.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}