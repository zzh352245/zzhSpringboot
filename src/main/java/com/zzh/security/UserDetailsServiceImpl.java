package com.zzh.security;

import com.zzh.mapper.SysPermissionMapper;
import com.zzh.model.SysPermission;
import com.zzh.model.SysUser;
import com.zzh.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：zz
 * @date ：Created in 2021/12/2 10:09
 * @description：用户认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPermissionMapper permissionMapper;

    /**
     * create by: zz
     * description: 登录获取用户信息
     * create time: 2021/12/2 10:10
     * @param: userName
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser sysUser = userService.loginByUserName(userName);
        if(sysUser == null){
            throw new RuntimeException("用户不存在");
        }
        List<SysPermission> permissions = permissionMapper.selectPerListByUser(sysUser.getId());
        UserDetail userDetail = new UserDetail(sysUser, permissions);
        return userDetail;
    }

}
