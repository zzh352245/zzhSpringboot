package com.zzh.vo;

import com.zzh.model.SysUser;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
* 账号管理视图实体类
*
* @author zzh
* @since 2021-12-01
*/
@Data
@ApiModel(value = "SysUserVO对象", description = "账号管理")
public class SysUserVO extends SysUser {
    private static final long serialVersionUID = 1L;

}
