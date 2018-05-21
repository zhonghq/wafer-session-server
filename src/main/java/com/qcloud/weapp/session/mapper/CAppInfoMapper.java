package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CAppInfo;

import java.util.List;

public interface CAppInfoMapper {
    int deleteByPrimaryKey(String appid);

    int insert(CAppInfo record);

    int insertSelective(CAppInfo record);

    /**
     * 查询小程序信息
     * @return
     */
    CAppInfo selectOne();

    int updateByPrimaryKeySelective(CAppInfo record);

    int updateByPrimaryKey(CAppInfo record);
}