package in.hocg.rabbit.generator.core;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hocgin on 2020/5/29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class CodeGenerator {

    public void generateByTables(DataSource dataSource, Module module, String... tables) {
        generateByTables(dataSource, module, false, tables);
    }

    /**
     * 根据表来生成代码
     *
     * @param dataSource   数据源
     * @param module       模块
     * @param fileOverride 是否覆盖文件
     * @param tables       要生成的表
     */
    public void generateByTables(DataSource dataSource, Module module, boolean fileOverride, String... tables) {
        String javaPath = "src/main/java";
        final Path rootPath = Paths.get(System.getProperty("user.dir"));
        String outputDir = rootPath.resolve(module.getRelativePath()).resolve(javaPath).toString();

//
//        config.setActiveRecord(true)
//            .setEnableCache(false)
//            .setBaseColumnList(false)
//            .setBaseResultMap(false)
//            .setServiceName("%sService");

        GlobalConfig.Builder configBuilder = new GlobalConfig.Builder()
            .disableOpenDir()
            .enableSwagger()
            .outputDir(outputDir)
            .author(System.getProperty("user.name"));
        if (fileOverride) {
            configBuilder = configBuilder.fileOverride();
        }
        GlobalConfig config = configBuilder.build();


        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
            dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword())
            .build();

        // == Base
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
            .enableCapitalMode()
            .addInclude(tables)
            .addTablePrefix(module.getIgnoreTablePrefix().toArray(new String[]{}))
            .build();
        strategyConfig.entityBuilder()
            .enableLombok()
            .enableActiveRecord()
            .addSuperEntityColumns("id",
                "created_at", "creator",
                "last_updated_at", "last_updater")
            .enableTableFieldAnnotation()
//                .enableColumnConstant()
            .superClass(CommonEntity.class)
            .naming(NamingStrategy.underline_to_camel)
            .build();
        strategyConfig.controllerBuilder()
            .enableHyphenStyle()
            .enableRestStyle()
            .build();
        strategyConfig.serviceBuilder()
            .formatServiceFileName("%sService")
//                .formatServiceImplFileName("%sServiceImpl")
            .superServiceClass(AbstractService.class)
            .superServiceImplClass(AbstractServiceImpl.class)
            .build();
//        strategyConfig
//            .setEntityLombokModel(true)
//            .setSuperEntityColumns("id",
//                "created_at", "creator",
//                "last_updated_at", "last_updater"
//            )
//            .setEntityTableFieldAnnotationEnable(true)
//            .setCapitalMode(true)
//            .setEntityColumnConstant(false)
//            .setControllerMappingHyphenStyle(true)
//            .setRestControllerStyle(true)
//            .setTablePrefix(module.getIgnoreTablePrefix().toArray(new String[]{}))
//            .setSuperEntityClass(CommonEntity.class.getName())
//            .setSuperServiceClass(AbstractService.class.getName())
//            .setSuperServiceImplClass(AbstractServiceImpl.class.getName())
//            .setNaming(NamingStrategy.underline_to_camel)
//            .setInclude(tables);
//        config.setOpen(false)
//            .setActiveRecord(true)
//            .setAuthor(System.getProperty("user.name"))
//            .setOutputDir(outputDir)
//            .setFileOverride(fileOverride)
//            .setEnableCache(false)
//            .setBaseColumnList(false)
//            .setBaseResultMap(false)
//            .setServiceName("%sService");

        new AutoGenerator(dataSourceConfig)
            .global(config)
            .strategy(strategyConfig)
            .template(new TemplateConfig.Builder()
                .controller("/template/mybatis/controller.java")
                .serviceImpl("/template/mybatis/serviceImpl.java")
                .mapper("/template/mybatis/mapper.java")
                .entity("/template/mybatis/entity.java")
                .build()
            )
            .packageInfo(
                new PackageConfig.Builder()
                    .parent(module.getPackageName())
                    .controller("controller")
                    .entity("entity")
                    .build()
            ).execute();

    }
}
