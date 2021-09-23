package com.Long.JavaWiki.job;

import com.Long.JavaWiki.service.DocService;
import com.Long.JavaWiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务:更新电子书信息
 */
@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 每5分钟更新一次电子书信息
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void cron() {
        //加入日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        //按电子书分组统计文档数据,并更新到对应的电子书中
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
