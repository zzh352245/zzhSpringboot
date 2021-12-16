package com.zzh.controller;

import com.zzh.api.Result;
import com.zzh.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 账号管理 前端控制器
 * </p>
 *
 * @author zzh
 * @since 2021-12-01
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @ApiOperation("获取用户列表")
    @GetMapping("/getUser")
    public Result getUser(@RequestParam(value = "pageNum") @ApiParam(value = "商家编码") Integer pageNum,
                          @RequestParam(value = "pageSize", required = false) @ApiParam(value = "商家编码") Integer pageSize) {
        if(pageSize == null){
            pageSize = 10;
        }
        return Result.data(sysUserService.queryUser(pageNum, pageSize));
    }

    @GetMapping("/test")
    public Result test() {
        return Result.success("hello world");
    }

    @GetMapping("/test1")
    public Result test1() {
        return Result.success("hello world");
    }

    @PostMapping("/login")
    public Result login(@RequestParam(value = "userName") String userName,
                        @RequestParam(value = "passWord") String passWord){
        return sysUserService.login(userName, passWord);
    }

}

