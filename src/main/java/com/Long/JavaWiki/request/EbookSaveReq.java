package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EbookSaveReq {
    private Long id;

    @NotNull(message = "[名称]不能为空")
    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;
}
