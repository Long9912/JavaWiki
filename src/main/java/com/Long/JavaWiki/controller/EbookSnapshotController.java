package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.response.StatisticResp;
import com.Long.JavaWiki.service.EbookSnapshotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 专栏快照 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
@Api("专栏快照控制类")
@RestController
@RequestMapping("/ebookSnapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @ApiOperation("获得首页使用的统计数值")
    @GetMapping("/getStatistic")
    private List<StatisticResp> getStatistic() {
        return ebookSnapshotService.getStatistic();
    }

    @ApiOperation("获得首页使用的统计数值")
    @GetMapping("/get30Statistic")
    private List<StatisticResp> get30DayStatistic() {
        return ebookSnapshotService.get30DayStatistic();
    }

    @ApiOperation("删除30天前的统计数据")
    @GetMapping("/delete30DayAgoData")
    private String delete30DayAgoData() {
        Integer count = ebookSnapshotService.delete30DayAgoData();
        return "删除30天前[统计数据]:" + count + "条";
    }
}

