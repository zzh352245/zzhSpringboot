package com.zzh.mapper;

import com.zzh.model.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author zzh
 * @since 2021-12-02
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectPerListByUser(@Param("userId") Long userId);

    List<SysPermission> selectListByPath(@Param("path") String path);

}
