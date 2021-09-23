package com.Long.JavaWiki.mapper;

import com.Long.JavaWiki.entity.EbookSnapshot;
import com.Long.JavaWiki.response.StatisticResp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 电子书快照 Mapper 接口
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {
    /**
     * 通过定时任务,生成电子书快照,得到总量,今日增长量
     */
    void genSnapshot();

    /**
     * 获得首页数据
     * @return 总阅读数,总点赞数,昨日今日阅读数,昨日今日点赞数
     */
    List<StatisticResp> getStatistic();

    /**
     * 获得30天新增数据统计
     */
    List<StatisticResp> get30DayStatistic();
}
