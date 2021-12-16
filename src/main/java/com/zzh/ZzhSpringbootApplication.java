package com.zzh;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author ：zz
 * @date ：Created in 2021/11/27 11:35
 * @description：启动类
 */
@EnableKnife4j
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ZzhSpringbootApplication {

    public static void main(String[] args){
        SpringApplication.run(ZzhSpringbootApplication.class, args);
    }

}
