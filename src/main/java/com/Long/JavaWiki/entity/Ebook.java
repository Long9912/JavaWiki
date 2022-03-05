package com.Long.JavaWiki.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 专栏
 * </p>
 *
 * @author Long9912
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("Ebook对象")
public class Ebook implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "专栏主题")
    private String name;

    @ApiModelProperty(value = "分类1")
    private Long category1Id;

    @ApiModelProperty(value = "分类2")
    private Long category2Id;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "文档数")
    private Integer docCount;

    @ApiModelProperty(value = "阅读数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞数")
    private Integer voteCount;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
