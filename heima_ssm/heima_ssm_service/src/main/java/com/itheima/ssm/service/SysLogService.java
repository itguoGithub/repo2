package com.itheima.ssm.service;


import com.itheima.ssm.domain.SysLog;

import java.util.List;

public interface SysLogService {
    //添加日志信息
    public void save(SysLog sysLog);
    //查询所有
    public List<SysLog> findAll();
}
