package com.ojbk.common.exception;

import lombok.Getter;

/**
 * 业务异常：Service 层主动抛出，由全局异常处理器统一转成 Result
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException( String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
