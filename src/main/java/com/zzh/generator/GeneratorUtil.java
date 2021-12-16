package com.zzh.generator;

/**
 * @author ：zz
 * @date ：Created in 2021/12/1 15:22
 * @description：生成数据库映射相关代码
 */
public class GeneratorUtil {

    /**
     * 调用生成代码方法 具体的 数据库配置信息 在 mybatis-plus.properties 文件
     * createController  是否创建 controller : true 为创建 false 为不创建
     * createService  是否创建 Service : true 为创建 false 为不创建
     * createServiceImpl  是否创建 ServiceImpl : true 为创建 false 为不创建
     * createEntity 是否创建 Entity : true 为创建 false 为不创建
     * createMapper 是否创建 Mapper : true 为创建 false 为不创建
     */
    public static void main(String[] args) {
        MybatisGenerator.codeGenerate(false,false,false,true,true);
    }

}
