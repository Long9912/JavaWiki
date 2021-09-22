package com.Long.JavaWiki.job;

import com.Long.JavaWiki.service.EbookSnapshotService;
import com.Long.JavaWiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @Autowired
    private SnowFlake snowFlake;

    //每6分钟更新一次电子书快照
    @Scheduled(cron = "0 */6 * * * ?")
    public void cron() {
        //加入日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        //按电子书分组统计文档数据,并更新到对应的电子书中
        LOG.info("生成今日电子书快照开始");
        long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("生成今日电子书快照结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
