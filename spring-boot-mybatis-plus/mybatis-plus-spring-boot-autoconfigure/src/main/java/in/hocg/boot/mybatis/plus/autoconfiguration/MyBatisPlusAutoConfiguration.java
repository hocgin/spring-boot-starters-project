package in.hocg.boot.mybatis.plus.autoconfiguration;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
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
import in.hocg.boot.mybatis.plus.extensions.MyBatisPlusExtensionsAutoConfiguration;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.web.autoconfiguration.properties.BootProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Set;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@Import(MyBatisPlusExtensionsAutoConfiguration.class)
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisPlusProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MyBatisPlusAutoConfiguration {
    private final MyBatisPlusProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

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
        return new BootMetaObjectHandler(contextHolder);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisContextHolder mybatisPlusContextHolder() {
        return new DefaultMybatisContextHolder();
    }

    @Bean
    @Primary
    public MybatisPlusProperties myBatisPlusProperties(MybatisPlusProperties properties, MetaObjectHandler metaObjectHandler) {
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


    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BootProperties.class, InetUtils.class})
    public static class IdentifierGeneratorConfiguration {
        @Bean
        @Primary
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public IdentifierGenerator identifierGenerator(BootProperties properties, InetUtils inetUtils) {
            Long workerId = properties.getWorkerId();
            Long datacenterId = properties.getDatacenterId();
            if (workerId != null && datacenterId != null) {
                return new DefaultIdentifierGenerator(workerId, datacenterId);
            } else {
                return new DefaultIdentifierGenerator(inetUtils.findFirstNonLoopbackAddress());
            }
        }
    }

}
