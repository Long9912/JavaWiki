package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-12
 */
@Data
public class CategorySaveReq  {

    private Long id;

    private Long parent;

    @NotNull(message = "[名称]不能为空")
    private String name;

    @NotNull(message = "[排序]不能为空")
    private Integer sort;


}
