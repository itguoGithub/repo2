package com.itheima.ssm.controller;
import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysLogService sysLogService;

    private Class clazz;
    private String methodName;
    private Method method;
    private Date visitTime;
    private String url="";
    private Long executeTime;
    private String username;
    private String ip;

    @Before("execution(* com.itheima.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint joinPoint) throws NoSuchMethodException {
        //获取当前时间
        visitTime=new Date();
        //获取当前类
        clazz = joinPoint.getTarget().getClass();
        //获取当前方法名
        methodName = joinPoint.getSignature().getName();
        //获取当前方法的参数
        Object[] args = joinPoint.getArgs();
        //获取方法
        if(args!=null&&args.length!=0){
            Class[] classes=new Class[args.length];
            for (int i=0;i<args.length;i++){
                classes[i]=args[i].getClass();
            }
            method = clazz.getMethod(methodName, classes);
        }else{
            method=clazz.getMethod(methodName);
        }
    }

    @After("execution(* com.itheima.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint joinPoint){
        //获取访问时间
        executeTime=new Date().getTime()-visitTime.getTime();
        //获取url
        if(clazz!=null&&method!=null&&clazz!=LogAop.class){
            RequestMapping annotation = (RequestMapping)clazz.getAnnotation(RequestMapping.class);
            if(annotation!=null){
                String[] value = annotation.value();
                url+=value[0];
            }
            RequestMapping annotation1 = method.getAnnotation(RequestMapping.class);
            if(annotation1!=null){
                String[] value = annotation1.value();
                url+=value[0];
            }
        }
        //获取ip地址
        ip = request.getRemoteAddr();
        //获取当前用户名
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User)context.getAuthentication().getPrincipal();
        username = user.getUsername();

        SysLog sysLog=new SysLog();
        sysLog.setVisitTime(visitTime);
        sysLog.setUsername(username);
        sysLog.setIp(ip);
        sysLog.setUrl(url);
        sysLog.setExecutionTime(executeTime);
        sysLog.setMethod("[类名] "+clazz.getName()+"[方法名 ]"+methodName);

        sysLogService.save(sysLog);
    }

}
