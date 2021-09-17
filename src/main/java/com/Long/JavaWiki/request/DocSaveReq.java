package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class DocSaveReq {

    private Long id;

    @NotNull(message = "[电子书Id]不能为空")
    private Long ebookId;

    @NotNull(message = "[父文档]不能为空")
    private Long parent;

    @NotEmpty(message = "[名称]不能为空")
    private String name;

    @NotEmpty(message = "[排序]不能为空")
    private Integer sort;

    @NotEmpty(message = "[内容]不能为空")
    private String content;

    private Integer viewCount;

    private Integer voteCount;

    private Integer deleted;


}
