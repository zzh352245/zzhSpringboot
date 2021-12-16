package com.zzh.filter;

import com.zzh.config.PermitConfig;
import com.zzh.security.UserDetail;
import com.zzh.util.RedisUtil;
import com.zzh.util.SM2Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：zz
 * @date ：Created in 2021/12/10 15:13
 * @description：登录校验
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SM2Util sm2Util;

    @Autowired
    private RedisUtil redisUtil;

    private static PermitConfig permitConfig;

    @Autowired
    public void setPermitConfig(PermitConfig permitConfig){
        this.permitConfig = permitConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String path : permitConfig.getUrls()) {
            if(pathMatcher.match(path, uri)){
                filterChain.doFilter(request, response);
                return;
            }
        }
        String authHeader = request.getHeader("token");
        if(StringUtils.isNotBlank(authHeader)){
            UserDetail userDetail = sm2Util.decryptToUser(authHeader);
            if(userDetail != null){
                String token = redisUtil.get(userDetail.getUsername());
                if(!StringUtils.isBlank(token)){
                    if(StringUtils.equals(token, authHeader)){
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken); //登录状态
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
