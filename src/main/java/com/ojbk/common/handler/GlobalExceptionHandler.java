package com.ojbk.common.handler;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.ojbk.common.Result;
import com.ojbk.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e){
        log.warn("业务异常：{}",e.getMessage());
        return Result.fail(e.getMessage(),e.getCode());
    }

    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleNotLoginException(NotLoginException e){
        log.warn("用户未登录：{}",e.getMessage());
        return Result.fail(e.getMessage(),401);
    }

    @ExceptionHandler(NotPermissionException.class)
    public Result<Void> handleNOtPermissionException(NotPermissionException e){

        log.warn("权限不足：{}",e.getMessage());
        return Result.fail(e.getMessage(),403);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("参数校验失败：{}",e.getMessage());
        return Result.fail(e.getMessage(),400);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){

        log.warn("未知错误：{}",e.getMessage());
        return Result.fail("未知错误，请联系管理员",500);

    }

}
