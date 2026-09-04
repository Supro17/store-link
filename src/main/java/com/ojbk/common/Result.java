package com.ojbk.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "统一响应结果")
public class Result<T> {

    //0是正常 ,403权限不够，
    // 业务码：200 成功，400 参数错，401 未登录，409 乐观锁冲突，500 系统错误
    @Schema(description = "业务码：200成功，400参数错，401未登录，403权限不够，409乐观锁冲突，500系统错误", example = "200")
    private Integer code;


    @Schema(description = "提示信息", example = "操作成功")
    private String message;

    @Schema(description = "响应数据")
    private  T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public  Result(){
        this.timestamp=System.currentTimeMillis();
    }

    /**
     * 默认成功
     * @param data
     * @return
     * @param <T>
     */

    public static <T> Result<T> success(T data){

        Result<T> tResult = new Result<>();

        tResult.data=data;
        tResult.message="操作成功";
        tResult.code=200;
        return tResult;
    }

    /**
     * 自定义提示词成功
     * @param message
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(String message,T data){

        Result<T> tResult = new Result<>();
        tResult.setCode(200);
        tResult.data=data;
        tResult.setMessage(message);

        return tResult;

    }


    /**
     * 自定义提示词失败
     * @param message
     * @param code
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(String message,Integer code){

        Result<T> tResult = new Result<>();
        tResult.code=code;
        tResult.message=message;
        return tResult;
    }

    /**
     * 默认失败
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(String message){

        Result<T> tResult = new Result<>();
        tResult.message=message;
        tResult.code=500;

        return tResult;
    }






}