package com.ojbk.service.Impl;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ojbk.entity.Role;
import com.ojbk.entity.RolePermission;
import com.ojbk.entity.UserRole;
import com.ojbk.mapper.RoleMapper;
import com.ojbk.mapper.RolePermissionMapper;
import com.ojbk.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class StpInterfaceImpl implements StpInterface {


    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    RoleMapper roleMapper;


    @Resource
    private RolePermissionMapper rolePermissionMapper;




    private static final Map<String ,List<String> > role_permission = new HashMap<>();
//
//    static {
//
//        //管理员的权限
//        role_permission.put("ADMIN",List.of("*"));
//
//        //总部的权限，拥有所有业务操作权限
//        role_permission.put("HQ", Arrays.asList(
//                "store:list", "store:create", "store:update","store:selectById","store:delete","store:export","storeline:dash",
//                "salesAssist:list", "salesAssist:create", "salesAssist:update","salesAssist:selectById","salesAssist:delete",
//                "verify:list", "verify:create", "verify:update","verify:selectById","verify:delete",
//                "transfer:list", "transfer:create", "transfer:update","transfer:selectById","transfer:delete",
//                "perf:list", "perf:create", "perf:update","perf:selectById","perf:delete",
//                "audit:list", "audit:create", "audit:update","audit:selectById","audit:delete",
//                "user:list","user:create","user:update","user:delete","user:selectuserbyid","storeline:dash"
//        ));
//        //店长的权限
//        role_permission.put("STORE_MANAGER",Arrays.asList(
//                "store:update","store:list","salesAssist:list","verify:list","perf:list","audit:list","store:export","storeline:dash",
//                "audit:list", "audit:update","transfer:list","salesAssist:create", "salesAssist:update","salesAssist:selectById","salesAssist:delete",
//                "perf:create", "perf:update","perf:delete","salesAssist:delete","store:selectById",
//                "user:list","user:create","user:update","user:delete","user:selectuserbyid","storeline:dash"
//
//        ));
//
//        //导购的权限，
//        role_permission.put("GUIDE",Arrays.asList(
//                "verify:list", "perf:list","salesAssist:list","storeline:dash","storeline:dash"
//        ));
//
//
//        //督导的权限
//        role_permission.put("SUPERVISOR",Arrays.asList(
//                "audit:list", "perf:list","audit:create","storeline:dash",
//                "audit:update","audit:delete","storeline:dash"
//        ));
//    }


    /**
     * 获取用户角色的权限
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 用户的权限
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<String> roleList = getRoleList(loginId, loginType);

        if (Objects.isNull(roleList)){
            return Collections.emptyList();
        }

        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Role> wrapper1 = new LambdaQueryWrapper<>();



        wrapper1.in(Role::getRoleCode,roleList);
        List<Role> roles = roleMapper.selectList(wrapper1);

        if (roles == null){
            return Collections.emptyList();
        }

        List<Long> collect = roles.stream().map(Role::getRoleId).collect(Collectors.toList());


        wrapper.in(RolePermission::getRoleId,collect);

        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);

        List<String> collect1 = rolePermissions.stream().map(RolePermission::getPermission).collect(Collectors.toList());

        return collect1;



    }


    /**
     * 获取用户角色
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        Long userId = Long.valueOf(loginId.toString());

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(UserRole::getUserId,userId);

        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);

        if (userRoles.isEmpty()){
            return Collections.emptyList();
        }

        List<Long> roleList = userRoles.stream().map(UserRole::getRoleId).toList();

        LambdaQueryWrapper<Role> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(Role::getRoleId,roleList).eq(Role::getStatus,1);

        List<Role> roles = roleMapper.selectList(wrapper1);

        List<String> collect = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
        return collect;


    }




}