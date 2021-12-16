package com.zzh.vo;

import com.zzh.model.SysPermission;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
* 权限表视图实体类
*
* @author zzh
* @since 2021-12-02
*/
@Data
@ApiModel(value = "SysPermissionVO对象", description = "权限表")
public class SysPermissionVO extends SysPermission {
    private static final long serialVersionUID = 1L;

}
