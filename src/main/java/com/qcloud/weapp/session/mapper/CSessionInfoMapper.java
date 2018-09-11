package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CSessionInfo;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @author zhonghongqiang
 */
public interface CSessionInfoMapper {
    /**
     * 通过uuid删除Session
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新增Session
     * @param record
     * @return
     */
    int insert(CSessionInfo record);

    /**
     * 新增Session
     * @param record
     * @return
     */
    int insertSelective(CSessionInfo record);

    /**
     * 通过uuid查询session
     * @param id
     * @return
     */
    CSessionInfo selectByPrimaryKey(String id);

    /**
     * 通过openId查询Session
     * @param openId
     * @return
     */
    CSessionInfo selectByOpenId(String openId);

    /**
     * 通过uuid更新Session
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CSessionInfo record);

    /**
     * 通过uuid更新LastVisitTime
     * @param record
     * @return
     */
    int updateLastVisitTime(CSessionInfo record);

    /**
     * 通过uuid更新Session
     * @param record
     * @return
     */
    int updateByPrimaryKey(CSessionInfo record);
}