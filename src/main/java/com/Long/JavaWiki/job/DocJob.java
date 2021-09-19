package com.Long.JavaWiki.job;

import com.Long.JavaWiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    DocService docService;

    //每分钟更新一次电子书信息
    @Scheduled(cron = "0 */1 * * * ?")
    public void cron() {
        //按电子书分组统计文档数据,并更新到对应的电子书中
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
