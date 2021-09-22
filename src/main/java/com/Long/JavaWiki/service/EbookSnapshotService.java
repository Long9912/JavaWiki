package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.EbookSnapshot;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 电子书快照 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-22
 */
public interface EbookSnapshotService extends IService<EbookSnapshot> {

    void genSnapshot();
}
