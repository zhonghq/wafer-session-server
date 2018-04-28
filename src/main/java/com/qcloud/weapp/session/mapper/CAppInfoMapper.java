package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CAppInfo;

public interface CAppInfoMapper {
    int deleteByPrimaryKey(String appid);

    int insert(CAppInfo record);

    int insertSelective(CAppInfo record);

    CAppInfo selectByPrimaryKey(String appid);

    int updateByPrimaryKeySelective(CAppInfo record);

    int updateByPrimaryKey(CAppInfo record);
}