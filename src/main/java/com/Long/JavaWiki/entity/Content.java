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
 * 文档内容
 * </p>
 *
 * @author Long9912
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Content对象", description="文档内容")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文档id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

}
