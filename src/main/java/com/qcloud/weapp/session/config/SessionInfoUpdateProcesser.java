package com.qcloud.weapp.session.config;

import com.qcloud.weapp.session.mapper.CSessionInfoMapper;
import com.qcloud.weapp.session.model.CSessionInfo;

/**
 * @author zhonghongqiang
 *         Created on 2018-09-11.
 */
public class SessionInfoUpdateProcesser implements Runnable {

    private CSessionInfoMapper cSessionInfoMapper;

    private CSessionInfo cSessionInfo;

    /**
     * 只更新访问时间
     */
    private Boolean justVisitTime;

    public SessionInfoUpdateProcesser(CSessionInfoMapper cSessionInfoMapper, CSessionInfo cSessionInfo ) {
        this.cSessionInfoMapper = cSessionInfoMapper;
        this.cSessionInfo = cSessionInfo;
        this.justVisitTime = false;
    }

    public SessionInfoUpdateProcesser(CSessionInfoMapper cSessionInfoMapper, CSessionInfo cSessionInfo , Boolean justVisitTime) {
        this.cSessionInfoMapper = cSessionInfoMapper;
        this.cSessionInfo = cSessionInfo;
        this.justVisitTime = justVisitTime;
    }

    @Override
    public void run() {
        if (justVisitTime) {
            cSessionInfoMapper.updateLastVisitTime(cSessionInfo);
        }else {
            cSessionInfoMapper.updateByPrimaryKeySelective(cSessionInfo);
        }
    }
}
