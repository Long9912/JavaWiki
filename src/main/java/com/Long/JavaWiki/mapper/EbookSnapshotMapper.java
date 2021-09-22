package com.Long.JavaWiki.mapper;

import com.Long.JavaWiki.entity.EbookSnapshot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 电子书快照 Mapper 接口
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {

    void genSnapshot();
}
