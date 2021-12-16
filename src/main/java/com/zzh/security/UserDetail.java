package com.zzh.security;

import com.zzh.model.SysPermission;
import com.zzh.model.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：zz
 * @date ：Created in 2021/12/2 10:57
 * @description：用户信息封装
 */
public class UserDetail implements UserDetails {

    private SysUser user;

    private List<SysPermission> permissions;

    public UserDetail(SysUser user, List<SysPermission> permissions){
        this.user = user;
        this.permissions = permissions;
    }

    public List<SysPermission> getPermissions(){
        return permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream().map(sysPermission -> new SimpleGrantedAuthority(sysPermission.getPermissionCode())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassWord();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public SysUser getUser(){
        return this.user;
    }
}
