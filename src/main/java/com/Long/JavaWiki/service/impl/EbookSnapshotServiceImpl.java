package com.Long.JavaWiki.service.impl;

import com.Long.JavaWiki.entity.EbookSnapshot;
import com.Long.JavaWiki.mapper.EbookSnapshotMapper;
import com.Long.JavaWiki.response.StatisticResp;
import com.Long.JavaWiki.service.EbookSnapshotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 电子书快照 服务实现类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot> implements EbookSnapshotService {

    @Autowired
    private EbookSnapshotMapper ebookSnapshotMapper;

    @Override
    public void genSnapshot() {
        ebookSnapshotMapper.genSnapshot();
    }

    /**
     * 获得首页数据: 总阅读数,总点赞数,今日阅读数,今日点赞数,今日预计阅读数,今日预计阅读增长
     */
    @Override
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapper.getStatistic();
    }
}
