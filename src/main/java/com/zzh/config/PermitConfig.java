package com.zzh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：zz
 * @date ：Created in 2021/12/10 15:41
 * @description：接口白名单
 */
@Data
@Component
@ConfigurationProperties(prefix = "permit")
public class PermitConfig {

    private List<String> urls;

}
