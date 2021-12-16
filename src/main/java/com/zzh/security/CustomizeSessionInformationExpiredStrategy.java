package com.zzh.security;

import com.alibaba.fastjson.JSON;
import com.zzh.api.Result;
import com.zzh.api.ResultCode;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：zz
 * @date ：Created in 2021/12/3 9:24
 * @description：会话过期
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        HttpServletResponse httpServletResponse = event.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.fail(ResultCode.UN_AUTHORIZED)));
    }
}
