package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CSessionInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhonghongqiang
 */
@CacheConfig(cacheNames = "sessionInfo")
public interface CSessionInfoMapper {
    /**
     * 通过uuid删除Session
     * @param id
     * @return
     */
    @CachePut(key = "#p0")
    int deleteByPrimaryKey(String id);

    /**
     * 新增Session
     * @param record
     * @return
     */
    @CachePut(key = "#p0.uuid")
    int insert(CSessionInfo record);

    /**
     * 新增Session
     * @param record
     * @return
     */
    @CachePut(key = "#p0.uuid")
    int insertSelective(CSessionInfo record);

    /**
     * 通过uuid查询session
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
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
    @CachePut(key = "#p0.uuid")
    int updateByPrimaryKeySelective(CSessionInfo record);

    /**
     * 通过uuid更新LastVisitTime
     * @param record
     * @return
     */
    @CachePut(key = "#p0.uuid")
    int updateLastVisitTime(CSessionInfo record);

    /**
     * 通过uuid更新Session
     * @param record
     * @return
     */
    @CachePut(key = "#p0.uuid")
    int updateByPrimaryKey(CSessionInfo record);
}