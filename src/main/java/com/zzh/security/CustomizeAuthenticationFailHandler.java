package com.zzh.security;

import com.alibaba.fastjson.JSON;
import com.zzh.api.Result;
import com.zzh.api.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：zz
 * @date ：Created in 2021/12/2 15:31
 * @description：登录失败处理逻辑
 */
@Component
public class CustomizeAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //返回json数据
         Result result = null;
        if (exception instanceof AccountExpiredException) {
            //账号过期
            result = Result.fail(ResultCode.UN_LOGIN);
        } else if (exception instanceof BadCredentialsException) {
            //密码错误
            result = Result.fail(ResultCode.PASSWORD_ERROR);
        } else if (exception instanceof CredentialsExpiredException) {
            //密码过期
            result = Result.fail(ResultCode.PASSWORD_ERROR);
        } else if (exception instanceof DisabledException) {
            //账号不可用
            result = Result.fail(ResultCode.UN_AUTHORIZED);
        } else if (exception instanceof LockedException) {
            //账号锁定
            result = Result.fail(ResultCode.UN_AUTHORIZED);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = Result.fail(ResultCode.UN_AUTHORIZED);
        }else{
            //其他错误
            result = Result.fail(ResultCode.FAILURE);
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));
    }
}
