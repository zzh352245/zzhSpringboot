package com.zzh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.api.Result;
import com.zzh.model.LoginUser;
import com.zzh.model.SysUser;
import com.zzh.mapper.SysUserMapper;
import com.zzh.security.UserDetail;
import com.zzh.service.ISysUserService;
import com.zzh.util.RedisUtil;
import com.zzh.util.SM2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号管理 服务实现类
 * </p>
 *
 * @author zzh
 * @since 2021-12-01
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SM2Util sm2Util;

    @Override
    public IPage<SysUser> queryUser(Integer pageNum, Integer pageSize){
        Page infoPage = new Page<>(pageNum, pageSize);
        IPage<SysUser> page = userMapper.selectPage(infoPage, new QueryWrapper<>());
        return page;
    }

    /**
     * create by: zz
     * description: 根据用户名获取账号信息
     * create time: 2021/12/2 10:26
     * @param: userName
     * @return com.zzh.model.SysUser
     */
    @Override
    public SysUser loginByUserName(String userName) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        SysUser user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return null;
        }
        return user;
    }

    /**
     * create by: zz
     * description: 修改账号信息
     * create time: 2021/12/2 15:27
     * @param: sysUser
     * @return java.lang.Integer
     */
    @Override
    public Integer updateUser(SysUser sysUser){
        userMapper.updateById(sysUser);
        return 1;
    }

    /**
     * create by: zz
     * description: 登录
     * create time: 2021/12/10 10:34
     * @param: userName
     * @param: passWord
     * @return com.zzh.api.Result
     */
    @Override
    public Result login(String userName, String passWord) {
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(userName);
        if(!passwordEncoder.matches(passWord, userDetail.getPassword())){
            return Result.fail("密码错误");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(userDetail.getUser());
        loginUser.setPermissions(userDetail.getPermissions());
        String token = sm2Util.encrypt(JSONObject.toJSONString(loginUser));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        redisUtil.setStringRedis(userDetail.getUsername(), token, 60*60*12);
        return Result.data(token);
    }

}
