package com.Long.JavaWiki.response;

import lombok.Data;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-12
 */
@Data
public class CategoryQueryResp {

    private Long id;

    private Long parent;

    private String name;

    private Integer sort;

}
