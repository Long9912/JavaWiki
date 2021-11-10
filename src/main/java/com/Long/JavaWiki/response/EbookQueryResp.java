package com.Long.JavaWiki.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;
}
