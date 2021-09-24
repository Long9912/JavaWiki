package com.Long.JavaWiki.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocQueryResp {

    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

}
