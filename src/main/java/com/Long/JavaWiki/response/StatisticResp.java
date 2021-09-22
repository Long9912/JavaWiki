package com.Long.JavaWiki.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticResp {

    private LocalDate date;

    private Integer viewCount;

    private Integer voteCount;

    private Integer viewIncrease;

    private Integer voteIncrease;
}
