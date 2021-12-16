package com.zzh.model;

import lombok.Data;

import java.util.List;

/**
 * @author ：zz
 * @date ：Created in 2021/12/14 18:21
 * @description：登录用户信息
 */
@Data
public class LoginUser {

    private SysUser user;

    private List<SysPermission> permissions;

}
