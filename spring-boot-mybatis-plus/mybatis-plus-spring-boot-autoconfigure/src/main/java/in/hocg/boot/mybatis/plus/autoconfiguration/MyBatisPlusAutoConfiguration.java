package in.hocg.boot.mybatis.plus.autoconfiguration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.google.common.collect.Sets;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.ColumnConstants;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.DefaultMybatisContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.MybatisContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.fill.BootMetaObjectHandler;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.tenant.BootTenantHandler;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.interceptor.LogicDeleteInterceptor;
import in.hocg.boot.mybatis.plus.autoconfiguration.properties.MyBatisPlusProperties;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.util.Set;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisPlusProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MyBatisPlusAutoConfiguration implements ApplicationContextAware {
    private final MyBatisPlusProperties properties;
    @Setter
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisContextHolder contextHolder) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (properties.getTenant().getEnable()) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler(properties, contextHolder)));
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new LogicDeleteInterceptor(contextHolder));
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantLineHandler tenantLineHandler(MyBatisPlusProperties properties, MybatisContextHolder contextHolder) {
        return new BootTenantHandler(properties, contextHolder);
    }


    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler(MybatisContextHolder contextHolder) {
        return new BootMetaObjectHandler(applicationContext, contextHolder);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisContextHolder mybatisPlusContextHolder() {
        return new DefaultMybatisContextHolder();
    }

    @Bean
    @Primary
    public MybatisPlusProperties myBatisPlusProperties(MybatisPlusProperties properties,
                                                       MetaObjectHandler metaObjectHandler) {
        GlobalConfig globalConfig = LangUtils.getOrDefault(properties.getGlobalConfig(), new GlobalConfig());
        globalConfig.setBanner(false);

        GlobalConfig.DbConfig dbConfig = LangUtils.getOrDefault(globalConfig.getDbConfig(), new GlobalConfig.DbConfig());
        dbConfig.setIdType(IdType.ASSIGN_ID);
        dbConfig.setLogicDeleteField(ColumnConstants.DELETED_AT);
        dbConfig.setLogicDeleteValue("NOW(6)");
        dbConfig.setLogicNotDeleteValue("NULL");
        globalConfig.setDbConfig(dbConfig);

        globalConfig.setMetaObjectHandler(metaObjectHandler);
        properties.setGlobalConfig(globalConfig);

        Set<String> locations = Sets.newHashSet(properties.getMapperLocations());
        locations.add("classpath*:/**/xml/*.xml");
        properties.setMapperLocations(locations.toArray(new String[]{}));
        return properties;
    }
}
