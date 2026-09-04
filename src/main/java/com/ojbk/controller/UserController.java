package com.ojbk.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.UserDTO;
import com.ojbk.service.UserService;
import com.ojbk.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户管理")
public class UserController {


    @Resource
    private UserService userService;

    @GetMapping("/list")
    @SaCheckPermission("user:list")
    @Operation(summary = "用户列表", description = "分页查询用户列表，支持关键字搜索")
    public Result<PageResult<UserVO>> userPage(@Parameter(description = "分页查询参数") PageDTO dto){

        return Result.success(userService.userPage(dto));

    }

    @PostMapping("/create")
    @SaCheckPermission("user:create")
    @Operation(summary = "创建用户", description = "创建新用户并分配角色，密码自动BCrypt加密")
    public Result<UserVO> userCreate(@RequestBody UserDTO dto){

        return Result.success(userService.userCreate(dto));
    }

    @PutMapping("/update")
    @SaCheckPermission("user:update")
    @Operation(summary = "更新用户", description = "更新用户信息及角色，密码为空则不修改")
    public Result<UserVO> userUpdate(@RequestBody UserDTO dto){
        return Result.success(userService.userUpdate(dto));
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("user:delete")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户并清除角色关联")
    public Result userDelete(@Parameter(description = "用户ID") @PathVariable Long id) {

        return userService.userDelete(id);

    }


    @GetMapping("/{id}")
    @SaCheckPermission("user:selectuserbyid")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息及角色")
    public Result<UserVO> selectUserById(@Parameter(description = "用户ID") @PathVariable Long id){

        return Result.success(userService.selectUserById(id));
    }



}