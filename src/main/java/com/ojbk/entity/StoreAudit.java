package com.ojbk.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_storelink_storeaudit")
@Schema(name = "StoreAudit" ,description = "巡店督导")
public class StoreAudit {

    @Schema(name = "storeId", title = "门店ID", description = "关联的门店标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long storeId;

    @Schema(name = "userId", title = "督导用户ID", description = "创建该巡店记录的督导用户ID，关联系统用户表", example = "5")
    private Long userId;

    @Schema(name = "saId", title = "督导ID", description = "巡店督导人员ID", example = "2001")
    @TableId(type = IdType.AUTO)
    private Long saId;

    @Schema(name = "score", title = "巡店评分", description = "巡店评分分数", example = "85")
    private Long score;

    @Schema(name = "issue", title = "问题编号", description = "巡店发现的问题", example = "排队太久")
    private String issue;

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