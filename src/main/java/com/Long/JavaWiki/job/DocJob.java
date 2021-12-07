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
 * 定时任务:更新笔记信息
 */
@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 每分钟更新一次笔记信息
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cron() {
        //加入日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        //按笔记分组统计文档数据,并更新到对应的笔记中
        LOG.info("更新笔记下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新笔记下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
