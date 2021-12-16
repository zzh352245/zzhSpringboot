package com.zzh.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author ：zz
 * @date ：Created in 2021/12/1 15:19
 * @description：mybatis自动生成工具类
 */
public class MybatisGenerator {

    static final ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis-plus");

    public MybatisGenerator() {
    }

    public static void main(String[] args) {
        codeGenerate(false, false, false, true, false);
    }

    public static void codeGenerate(boolean createController, boolean createService, boolean createServiceImpl, boolean createEntity, boolean createMapper) {
        AutoGenerator autoGenerator = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        String pjPath = resourceBundle.getString("projectPath");
        if (StringUtils.isBlank(pjPath)) {
            pjPath = System.getProperty("user.dir");
        }
        final String projectPath = pjPath;
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(resourceBundle.getString("author"));
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setBaseResultMap(true);
        gc.setIdType(IdType.ASSIGN_ID);
        autoGenerator.setGlobalConfig(gc);
        String dbType = resourceBundle.getString("dbType");
        String schemaName = resourceBundle.getString("schemaName");
        DataSourceConfig dsc = new DataSourceConfig();
        if (StringUtils.isNotBlank(dbType)) {
            dsc.setDbType(DbType.getDbType(dbType));
        }

        if (StringUtils.isNotBlank(schemaName)) {
            dsc.setSchemaName(schemaName);
        }

        dsc.setDriverName(resourceBundle.getString("driverName"));
        dsc.setUrl(resourceBundle.getString("url"));
        dsc.setUsername(resourceBundle.getString("userName"));
        dsc.setPassword(resourceBundle.getString("password"));
        dsc.setTypeConvert(new MybatisGenerator.MySqlTypeConvertCustom());
        autoGenerator.setDataSource(dsc);
        final String packageName = resourceBundle.getString("parent");
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("model");
        pc.setMapper("mapper");
        autoGenerator.setPackageInfo(pc);
        InjectionConfig cfg = new InjectionConfig() {
            public void initMap() {
            }
        };
        String templatePath = "/templates/mapper.xml.vm";
        List<FileOutConfig> focList = new ArrayList();
        focList.add(new FileOutConfig(templatePath) {
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper//" + tableInfo.getEntityName() + "Mapper" + ".xml";
            }
        });
        focList.add(new FileOutConfig("/templates/entityVO") {
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + packageName.replace(".", "/") + "/vo/" + tableInfo.getEntityName() + "VO" + ".java";
            }
        });
        focList.add(new FileOutConfig("/templates/entityDTO") {
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + packageName.replace(".", "/") + "/dto/" + tableInfo.getEntityName() + "DTO" + ".java";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        TemplateConfig templateConfig = new TemplateConfig();
        if (!createController) {
            templateConfig.setController(null);
        }

        if (!createService) {
            templateConfig.setService(null);
        }

        if (!createServiceImpl) {
            templateConfig.setServiceImpl(null);
        }

        if (!createEntity) {
            templateConfig.setEntity(null);
        }

        if (!createMapper) {
            templateConfig.setMapper(null);
        }

        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude(resourceBundle.getString("tableNames").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(new String[]{pc.getModuleName() + "_"});
        autoGenerator.setStrategy(strategy);
        autoGenerator.execute();
    }

    static class MySqlTypeConvertCustom extends MySqlTypeConvert {
        MySqlTypeConvertCustom() {
        }

        public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            String t = fieldType.toLowerCase();
            return t.contains("tinyint(1)") ? DbColumnType.INTEGER : super.processTypeConvert(globalConfig, fieldType);
        }
    }

}
