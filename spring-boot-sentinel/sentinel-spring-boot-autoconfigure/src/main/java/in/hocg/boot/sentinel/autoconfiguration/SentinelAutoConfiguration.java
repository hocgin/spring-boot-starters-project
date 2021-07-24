package in.hocg.boot.sentinel.autoconfiguration;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.google.common.collect.Lists;
import in.hocg.boot.sentinel.autoconfiguration.properties.SentinelProperties;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = SentinelProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(SentinelProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SentinelAutoConfiguration implements InitializingBean {
    private final SentinelProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @Async
    @Override
    public void afterPropertiesSet() throws Exception {
        // - 流量控制规则
        List<FlowRule> flowRules = Lists.newArrayList();
        properties.getFlowRules().forEach(value -> {
            FlowRule rule = new FlowRule(value.getResource());
            LangUtils.setIfNotNull(value.getGrade(), rule::setGrade);
            LangUtils.setIfNotNull(value.getCount(), rule::setCount);
            LangUtils.setIfNotNull(value.getLimitApp(), rule::setLimitApp);
            LangUtils.setIfNotNull(value.getStrategy(), rule::setStrategy);
            LangUtils.setIfNotNull(value.getControlBehavior(), rule::setControlBehavior);
            LangUtils.setIfNotNull(value.getClusterMode(), rule::setClusterMode);
            flowRules.add(rule);
        });
        FlowRuleManager.loadRules(flowRules);

        // - 熔断降级规则
        List<DegradeRule> degradeRules = Lists.newArrayList();
        properties.getDegradeRules().forEach(value -> {
            DegradeRule rule = new DegradeRule(value.getResource());
            LangUtils.setIfNotNull(value.getGrade(), rule::setGrade);
            LangUtils.setIfNotNull(value.getCount(), rule::setCount);
            LangUtils.setIfNotNull(value.getTimeWindow(), rule::setTimeWindow);
            LangUtils.setIfNotNull(value.getMinRequestAmount(), rule::setMinRequestAmount);
            LangUtils.setIfNotNull(value.getStatIntervalMs(), rule::setStatIntervalMs);
            LangUtils.setIfNotNull(value.getSlowRatioThreshold(), rule::setSlowRatioThreshold);
            degradeRules.add(rule);
        });
        DegradeRuleManager.loadRules(degradeRules);

        // - 系统保护规则
        List<SystemRule> systemRules = Lists.newArrayList();
        properties.getSystemRules().forEach(value -> {
            SystemRule rule = new SystemRule();
            LangUtils.setIfNotNull(value.getResource(), rule::setResource);
            LangUtils.setIfNotNull(value.getLimitApp(), rule::setLimitApp);
            LangUtils.setIfNotNull(value.getQps(), rule::setQps);
            LangUtils.setIfNotNull(value.getAvgRt(), rule::setAvgRt);
            LangUtils.setIfNotNull(value.getMaxThread(), rule::setMaxThread);
            LangUtils.setIfNotNull(value.getHighestSystemLoad(), rule::setHighestSystemLoad);
            LangUtils.setIfNotNull(value.getHighestCpuUsage(), rule::setHighestCpuUsage);
            systemRules.add(rule);
        });
        SystemRuleManager.loadRules(systemRules);

        // - 访问控制规则
        List<AuthorityRule> authorityRules = Lists.newArrayList();
        properties.getAuthorityRules().forEach(value -> {
            AuthorityRule rule = new AuthorityRule();
            LangUtils.setIfNotNull(value.getResource(), rule::setResource);
            LangUtils.setIfNotNull(value.getLimitApp(), rule::setLimitApp);
            LangUtils.setIfNotNull(value.getStrategy(), rule::setStrategy);
            authorityRules.add(rule);
        });
        AuthorityRuleManager.loadRules(authorityRules);
    }
}
