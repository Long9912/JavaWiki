package com.Long.JavaWiki.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EbookQueryResp {
    private Long id;

    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;

    private LocalDateTime createTime;
}
