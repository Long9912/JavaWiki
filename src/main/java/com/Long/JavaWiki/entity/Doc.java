package com.Long.JavaWiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 文档
 * </p>
 *
 * @author Long9912
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Doc对象", description="文档")
public class Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "电子书id")
    private Long ebookId;

    @ApiModelProperty(value = "父id")
    private Long parent;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "顺序")
    private Integer sort;

    @ApiModelProperty(value = "阅读数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞数")
    private Integer voteCount;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;


}
