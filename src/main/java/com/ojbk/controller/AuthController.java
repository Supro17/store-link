package com.ojbk.controller;


import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ojbk.common.Result;
import com.ojbk.dto.LoginDTO;
import com.ojbk.service.AuthService;
import com.ojbk.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户名和密码进行登录认证，返回token及用户信息")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO){
        LoginVO login = authService.login(loginDTO);

        if (!Objects.isNull(login)){

            return Result.success(login);
        }else {
           return Result.fail("认证失败！");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "退出登录，清除当前用户的会话信息")
    public Result<Void> logout(){
        StpUtil.logout();
        return Result.success(null);

    }

    @GetMapping("/info")
    @Operation(summary = "获取用户会话信息", description = "获取当前登录用户的会话信息")
    public Result<SaSession> info(){
        SaSession session = StpUtil.getSession();
        return Result.success(session);
    }

}