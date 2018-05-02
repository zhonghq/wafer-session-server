package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CAppInfo;

import java.util.List;

public interface CAppInfoMapper {
    int deleteByPrimaryKey(String appid);

    int insert(CAppInfo record);

    int insertSelective(CAppInfo record);

    CAppInfo selectByPrimaryKey(String appid);

    /**
     * 查询小程序信息
     * @return
     */
    List<CAppInfo> selectAll();

    int updateByPrimaryKeySelective(CAppInfo record);

    int updateByPrimaryKey(CAppInfo record);
}