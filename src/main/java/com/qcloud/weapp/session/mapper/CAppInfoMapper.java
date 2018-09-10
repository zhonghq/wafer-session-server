package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CAppInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;


/**
 * @author zhonghongqiang
 */
@CacheConfig(cacheNames = "appInfo")
public interface CAppInfoMapper {

    @CachePut(key = "#p0")
    int deleteByPrimaryKey(String appid);

    @CachePut(key = "#p0.appid")
    int insert(CAppInfo record);

    @CachePut(key = "#p0.appid")
    int insertSelective(CAppInfo record);

    /**
     * 查询小程序信息
     * @return
     */
    @Cacheable(key = "#p0")
    CAppInfo selectByPrimaryKey(String appid);

    @CachePut(key = "#p0.appid")
    int updateByPrimaryKeySelective(CAppInfo record);

    @CachePut(key = "#p0.appid")
    int updateByPrimaryKey(CAppInfo record);
}