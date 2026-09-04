package com.ojbk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ojbk.entity.UserRole;
import com.ojbk.mapper.UserRoleMapper;
import com.ojbk.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
