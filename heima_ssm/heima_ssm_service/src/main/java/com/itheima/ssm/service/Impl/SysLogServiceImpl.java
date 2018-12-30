package com.itheima.ssm.service.Impl;

import com.itheima.ssm.dao.SysLogDao;
import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    /**
     * 添加日志信息
     * @param sysLog
     */
    @Override
    public void save(SysLog sysLog) {
        sysLogDao.save(sysLog);
    }

    /**
     * 查询所有日志信息
     * @return
     */
    @Override
    public List<SysLog> findAll() {
        return sysLogDao.findAll();
    }


}
