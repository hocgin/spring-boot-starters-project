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


        GlobalConfig config = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(dataSource.getDbType())
            .setUrl(dataSource.getUrl())
            .setUsername(dataSource.getUsername())
            .setPassword(dataSource.getPassword())
            .setDriverName(dataSource.getDriverName().getName());
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setEntityLombokModel(true)
            .setSuperEntityColumns("id",
                "created_at", "creator",
                "last_updated_at", "last_updater"
            )
            .setEntityTableFieldAnnotationEnable(true)
            .setCapitalMode(true)
            .setEntityColumnConstant(false)
            .setControllerMappingHyphenStyle(true)
            .setRestControllerStyle(true)
            .setTablePrefix(module.getIgnoreTablePrefix().toArray(new String[]{}))
            .setSuperEntityClass(CommonEntity.class.getName())
            .setSuperServiceClass(AbstractService.class.getName())
            .setSuperServiceImplClass(AbstractServiceImpl.class.getName())
            .setNaming(NamingStrategy.underline_to_camel)
            .setInclude(tables);
        config.setOpen(false)
            .setActiveRecord(true)
            .setAuthor(System.getProperty("user.name"))
            .setOutputDir(outputDir)
            .setFileOverride(fileOverride)
            .setEnableCache(false)
            .setBaseColumnList(false)
            .setBaseResultMap(false)
            .setServiceName("%sService");

        new AutoGenerator()
            .setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setTemplate(new TemplateConfig()
                .setController("/template/mybatis/controller.java")
                .setServiceImpl("/template/mybatis/serviceImpl.java")
                .setMapper("/template/mybatis/mapper.java")
                .setEntity("/template/mybatis/entity.java")
            )
            .setPackageInfo(
                new PackageConfig()
                    .setParent(module.getPackageName())
                    .setController("controller")
                    .setEntity("entity")
            ).execute();
    }
}
