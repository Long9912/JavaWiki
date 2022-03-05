package com.Long.JavaWiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 专栏快照
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EbookSnapshot对象", description="专栏快照")
public class EbookSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "专栏id")
    private Long ebookId;

    @ApiModelProperty(value = "快照日期")
    private LocalDate date;

    @ApiModelProperty(value = "阅读数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞数")
    private Integer voteCount;

    @ApiModelProperty(value = "阅读增长")
    private Integer viewIncrease;

    @ApiModelProperty(value = "点赞增长")
    private Integer voteIncrease;


}
