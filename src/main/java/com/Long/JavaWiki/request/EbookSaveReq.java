package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EbookSaveReq {
    private Long id;

    @NotEmpty(message = "[名称]不能为空")
    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;
}
