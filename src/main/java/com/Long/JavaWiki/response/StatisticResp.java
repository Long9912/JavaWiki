package com.Long.JavaWiki.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticResp {

    @JsonFormat(pattern = "MM-dd",timezone = "GMT+8")
    private LocalDate date;

    private Integer viewCount;

    private Integer voteCount;

    private Integer viewIncrease;

    private Integer voteIncrease;
}
