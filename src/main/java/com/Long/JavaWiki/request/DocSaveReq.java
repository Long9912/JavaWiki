package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class DocSaveReq {

    private Long id;

    @NotNull(message = "[专栏Id]不能为空")
    private Long ebookId;

    @NotNull(message = "[父文档]不能为空")
    private Long parent;

    @NotEmpty(message = "[名称]不能为空")
    private String name;

    @NotNull(message = "[排序]不能为空")
    private Integer sort;

    private String content;

    private Integer viewCount;

    private Integer voteCount;

}
