package com.ojbk.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.common.exception.BusinessException;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.UserDTO;
import com.ojbk.entity.*;
import com.ojbk.mapper.*;
import com.ojbk.service.UserService;
import com.ojbk.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private SalesAssistMapper salesAssistMapper;

    @Resource
    private StoreAuditMapper storeAuditMapper;

    @Resource
    private StoreMapper storeMapper;

    private void fillRoleInfo(Long userId,UserVO vo){

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(UserRole::getUserId,userId);

        UserRole userRoles = userRoleMapper.selectOne(wrapper);

        if (!Objects.isNull(userRoles)){
            Role role = roleMapper.selectById(userRoles.getRoleId());
            vo.setRoleCode(role.getRoleCode());
            vo.setRoleName(role.getRoleName());

        }


    }


    @Override
    public PageResult<UserVO> userPage(PageDTO dto) {

        IPage<User> page = new Page<>(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(dto.getKeyword()),User::getUserName,dto.getKeyword())
                .eq(StringUtils.hasText(dto.getStatus()),User::getStatus,dto.getStatus());

        Object roleCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");

        if (!"ADMIN".equals(roleCode) && !"HQ".equals(roleCode) ){
            if (!Objects.isNull(storeId)){
                wrapper.eq(User::getStoreId,storeId);
            }
        }

        IPage<User> users = userMapper.selectPage(page, wrapper);

        List<UserVO> collect = users.getRecords().stream().map(e -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(e, userVO);
            fillRoleInfo(e.getUserId(),userVO);
            return userVO;
        }).collect(Collectors.toList());

        PageResult<UserVO> userVOPage = new PageResult<>();

        userVOPage.setList(collect);
        userVOPage.setPage(users.getPages());
        userVOPage.setSize(users.getSize());
        userVOPage.setTotal(users.getTotal());

        return userVOPage;
    }

    @Override
    @Transactional
    public UserVO userCreate(UserDTO dto) {

        checkRolePermission(dto.getRoleId());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStoreId,dto.getStoreId())
                .eq(User::getUserName,dto.getUserName());





        User users = userMapper.selectOne(wrapper);

        if (!Objects.isNull(users)){
            throw new BusinessException("该用户已存在");
        }

        User user1 = new User();

        user1.setStoreId(dto.getStoreId());
        user1.setUserName(dto.getUserName());
        user1.setStatus(dto.getStatus());
        user1.setPhone(dto.getPhone());
        user1.setRealName(dto.getRealName());
        user1.setPassword(BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt()));

        userMapper.insert(user1);




        if (dto.getRoleId() != null){
            Role role = roleMapper.selectById(dto.getRoleId());

            if (role != null && "GUIDE".equals(role.getRoleCode())){


                SalesAssist salesAssist = new SalesAssist();

                salesAssist.setStoreId(dto.getStoreId());
                salesAssist.setName(dto.getRealName());
                salesAssist.setSales(BigDecimal.ZERO);
                salesAssistMapper.insert(salesAssist);


            }

        }

        if (dto.getRoleId() != null){
            Role role = roleMapper.selectById(dto.getRoleId());

            if (role != null && "SUPERVISOR".equals(role.getRoleCode())){

                StoreAudit storeAudit = new StoreAudit();

                storeAudit.setIssue(null);
                storeAudit.setUserId(user1.getUserId());
                storeAudit.setStoreId(dto.getStoreId());
                storeAudit.setScore(0L);
                storeAuditMapper.insert(storeAudit);

            }

        }

        if (dto.getRoleId() != null){
            Role role = roleMapper.selectById(dto.getRoleId());

            if (role != null && "STORE_MANAGER".equals(role.getRoleCode())){

                if (dto.getStoreId() == null){
                    throw new BusinessException("创建店长必须指定ID");
                }

                Store store = storeMapper.selectById(dto.getStoreId());
                if (Objects.isNull(store)){
                    throw new BusinessException("指定门店不存在");
                }
            }

        }


        UserRole userRole = new UserRole();
        userRole.setUserId(user1.getUserId());
        userRole.setRoleId(dto.getRoleId());
        userRoleMapper.insert(userRole);

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user1,vo);

        fillRoleInfo(user1.getUserId(),vo);

        return vo;


    }

    @Override
    @Transactional
    public UserVO userUpdate(UserDTO dto) {

        checkRolePermission(dto.getRoleId());

        User user = userMapper.selectById(dto.getUserId());

        if (Objects.isNull(user)){
            throw new BusinessException("该用户不存在");
        }


        User user1 = new User();
        user1.setUserId(user.getUserId());
        user1.setVersion(user.getVersion());
        user1.setStoreId(dto.getStoreId());
        user1.setUserName(dto.getUserName());
        user1.setStatus(dto.getStatus());
        user1.setPhone(dto.getPhone());
        user1.setRealName(dto.getRealName());

        if (StringUtils.hasText(dto.getPassword())){
            user1.setPassword(BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt()));
        }else {
            user1.setPassword(user.getPassword());
        }

        userMapper.updateById(user1);

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(UserRole::getUserId,dto.getUserId());

        UserRole userRole1 = userRoleMapper.selectOne(wrapper);

        if (!Objects.isNull(userRole1)){
            userRole1.setUserId(dto.getUserId());
            userRole1.setRoleId(dto.getRoleId());
            userRoleMapper.updateById(userRole1);
        }else {
            UserRole userRole = new UserRole();
            userRole.setUserId(dto.getUserId());
            userRole.setRoleId(dto.getRoleId());
            userRoleMapper.insert(userRole);
        }





        UserVO vo = new UserVO();

        BeanUtils.copyProperties(user1,vo);
        fillRoleInfo(user1.getUserId(),vo);

        return vo;
    }

    @Override
    @Transactional
    public Result userDelete(Long id) {

        removeById(id);

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);

        userRoleMapper.delete(wrapper);

        return Result.success("删除成功",null);

    }



    private void checkRolePermission(Long roleId){

        Object currentRoleCode= StpUtil.getSession().get("roleCode");

        if ("GUIDE".equals(currentRoleCode) || "SUPERVISOR".equals(currentRoleCode)){
            throw new BusinessException("权限不足");
        }

        if (!"ADMIN".equals(currentRoleCode)){

            if ("HQ".equals(currentRoleCode)){
                Role role = roleMapper.selectById(roleId);
                if (!Objects.isNull(role)){
                    if (!"HQ".equals(role.getRoleCode()) && !"STORE_MANAGER".equals(role.getRoleCode()) && !"GUIDE".equals(role.getRoleCode()) && !"SUPERVISOR".equals(role.getRoleCode())){

                        throw new BusinessException("权限不足");
                    }

                }
            }
            if ("STORE_MANAGER".equals(currentRoleCode)){
                Role role = roleMapper.selectById(roleId);
                if (!Objects.isNull(role)){
                    if (!"GUIDE".equals(role.getRoleCode())){
                        throw new BusinessException("权限不足");
                    }
                }

            }
        }
    }

    @Override
    public UserVO selectUserById(Long id) {

        User user = userMapper.selectById(id);

        if (Objects.isNull(user)){
            throw new BusinessException("用户不存在");
        }

        UserVO vo = new UserVO();

        BeanUtils.copyProperties(user,vo);

        fillRoleInfo(user.getUserId(),vo);
        return vo;

    }
}
