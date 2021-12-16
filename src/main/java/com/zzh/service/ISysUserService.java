package com.zzh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzh.api.Result;
import com.zzh.model.SysUser;

/**
 * <p>
 * 账号管理 服务类
 * </p>
 *
 * @author zzh
 * @since 2021-12-01
 */
public interface ISysUserService {

    SysUser loginByUserName(String userName);

    Integer updateUser(SysUser sysUser);

    IPage<SysUser> queryUser(Integer pageNum, Integer pageSize);

    Result login(String userName, String passWord);

}
