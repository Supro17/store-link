package com.ojbk.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_storelink_store")
@Schema(name = "Store" ,description = "门店档案")
public class Store {



    @Schema(name = "storeId", title = "门店ID", description = "门店唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @TableId(type= IdType.AUTO)
    private Long storeId;

    @Schema(name = "name", title = "门店名称", description = "门店的名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京旗舰店")
    private String name;

    @Schema(name = "city", title = "所在城市", description = "门店所在城市", example = "北京")
    private String city;

    @Schema(name = "status", title = "门店状态", description = "门店当前状态", example = "营业中")
    private String status;

    @Schema(name = "createdAt", title = "创建时间", description = "记录创建时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @Schema(name = "updatedAt", title = "更新时间", description = "记录最后更新时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;

    @Schema(name = "Deleted", title = "逻辑删除", description = "软删除标记：0-正常，1-已删除", hidden = true)
    @TableLogic
    private Integer deleted;

    @Version
    @Schema(name = "version", title = "乐观锁版本号", description = "用于乐观锁控制，更新时自动递增", example = "0")
    private Long version;




}