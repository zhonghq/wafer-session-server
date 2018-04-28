package com.qcloud.weapp.session.mapper;

import com.qcloud.weapp.session.model.CSessionInfo;

public interface CSessionInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CSessionInfo record);

    int insertSelective(CSessionInfo record);

    CSessionInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CSessionInfo record);

    int updateByPrimaryKey(CSessionInfo record);
}