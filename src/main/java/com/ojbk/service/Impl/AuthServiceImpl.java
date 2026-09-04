package com.ojbk.service.Impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ojbk.common.exception.BusinessException;
import com.ojbk.dto.LoginDTO;
import com.ojbk.entity.Role;
import com.ojbk.entity.User;
import com.ojbk.entity.UserRole;
import com.ojbk.mapper.RoleMapper;
import com.ojbk.mapper.UserMapper;
import com.ojbk.mapper.UserRoleMapper;
import com.ojbk.vo.LoginVO;
import com.ojbk.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public LoginVO login(LoginDTO loginDTO) {

        LambdaQueryWrapper<User> wrapper2= new LambdaQueryWrapper<>();
        wrapper2.eq(User::getUserName,loginDTO.getUserName());


        User user = userMapper.selectOne(wrapper2);
        if (Objects.isNull(user)){
           throw new BusinessException("账号不存在");
        }

        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())){
           throw new BusinessException("账号或密码错误");

        }



        if (user.getStatus() != 1){
           throw new BusinessException("账号已停用");
        }

        StpUtil.login(user.getUserId());
        SaSession session = StpUtil.getSession();



        if (user.getUserId()!=null){

            session.set("userId",user.getUserId());
        }

        if (user.getRealName()!=null){

            session.set("realName",user.getRealName());
        }

        if (user.getStoreId()!= null){
            session.set("storeId",user.getStoreId());
        }



        LambdaQueryWrapper<UserRole> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(UserRole::getUserId,user.getUserId());

        UserRole userRole = userRoleMapper.selectOne(wrapper1);


        String roleCode = null;
        if (userRole != null){
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null){
                roleCode = role.getRoleCode();
                session.set("roleCode",role.getRoleCode());
            }
        }
        LoginVO vo = new LoginVO();
        vo.setToken(StpUtil.getTokenValue());
        vo.setUserId(user.getUserId());
        vo.setRealName(user.getRealName());
        vo.setRoleCode(roleCode);
        vo.setStoreId(user.getStoreId());

        return vo;
    }
}