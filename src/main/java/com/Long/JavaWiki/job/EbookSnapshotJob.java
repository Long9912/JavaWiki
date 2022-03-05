package com.Long.JavaWiki.job;

import com.Long.JavaWiki.service.EbookSnapshotService;
import com.Long.JavaWiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务:更新专栏快照
 */
@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 每分钟更新一次专栏快照
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cron() {
        //加入日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        //使用复杂sql收集专栏数据和每日新增阅读量和点赞数
        LOG.info("生成今日专栏快照开始");
        long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("生成今日专栏快照结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
