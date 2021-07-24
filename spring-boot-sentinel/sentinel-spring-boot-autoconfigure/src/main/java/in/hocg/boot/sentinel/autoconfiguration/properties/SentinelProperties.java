package in.hocg.boot.sentinel.autoconfiguration.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(SentinelProperties.PREFIX)
public class SentinelProperties {
    public static final String PREFIX = "boot.sentinel";

    /**
     * 流量控制规则
     * <a href="https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6%E8%A7%84%E5%88%99-flowrule">流量控制规则 (FlowRule)</a>
     */
    private List<FlowRuleConfig> flowRules = Collections.emptyList();
    /**
     * 熔断降级规则 (DegradeRule)
     * <a href="https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E7%86%94%E6%96%AD%E9%99%8D%E7%BA%A7%E8%A7%84%E5%88%99-degraderule">熔断降级规则 (DegradeRule)</a>
     */
    private List<DegradeRuleConfig> degradeRules = Collections.emptyList();
    /**
     * 系统保护规则 (SystemRule)
     * <a href="https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E7%B3%BB%E7%BB%9F%E4%BF%9D%E6%8A%A4%E8%A7%84%E5%88%99-systemrule">系统保护规则 (SystemRule)</a>
     */
    private List<SystemRuleConfig> systemRules = Collections.emptyList();
    /**
     * 访问控制规则 (AuthorityRule)
     * <a href="https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E8%AE%BF%E9%97%AE%E6%8E%A7%E5%88%B6%E8%A7%84%E5%88%99-authorityrule">访问控制规则 (AuthorityRule)</a>
     */
    private List<AuthorityRuleConfig> authorityRules = Collections.emptyList();

    @Data
    @Accessors(chain = true)
    public static class AuthorityRuleConfig {
        /**
         * 资源名，即规则的作用对象
         */
        private String resource;
        /**
         * 流控针对的调用来源
         */
        private String limitApp;
        /**
         * 限制模式，AUTHORITY_WHITE 为白名单模式，AUTHORITY_BLACK 为黑名单模式，默认为白名单模式
         */
        private Integer strategy;
    }

    @Data
    @Accessors(chain = true)
    public static class SystemRuleConfig {
        /**
         * 资源名，即规则的作用对象
         */
        private String resource;
        /**
         * load1 触发值，用于触发自适应控制阶段
         */
        private Double highestSystemLoad;
        /**
         * 所有入口流量的平均响应时间
         */
        private Long avgRt;
        /**
         * 入口流量的最大并发数
         */
        private Long maxThread;
        /**
         * 所有入口资源的 QPS
         */
        private Double qps;
        /**
         * 当前系统的 CPU 使用率（0.0-1.0）
         */
        private Double highestCpuUsage;
        /**
         * 流控针对的调用来源
         */
        private String limitApp;
    }

    @Data
    @Accessors(chain = true)
    public static class DegradeRuleConfig {
        /**
         * 资源名，即规则的作用对象
         */
        private String resource;
        /**
         * 熔断策略，支持慢调用比例/异常比例/异常数策略
         */
        private Integer grade;
        /**
         * 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
         */
        private Double count;
        /**
         * 熔断时长，单位为 s
         */
        private Integer timeWindow;
        /**
         * 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
         */
        private Integer minRequestAmount;
        /**
         * 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
         */
        private Integer statIntervalMs;
        /**
         * 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
         */
        private Double slowRatioThreshold;
    }

    @Data
    @Accessors(chain = true)
    public static class FlowRuleConfig {
        /**
         * 资源名，即规则的作用对象
         */
        private String resource;
        /**
         * 限流阈值类型
         *
         * @see com.alibaba.csp.sentinel.slots.block.RuleConstant
         */
        private Integer grade;
        /**
         * 限流阈值
         */
        private Double count;
        /**
         * 流控针对的调用来源
         */
        private String limitApp;
        /**
         * 调用关系限流策略：直接、链路、关联
         */
        private Integer strategy;
        /**
         * 流控效果（直接拒绝/WarmUp/匀速+排队等待），不支持按调用关系限流
         */
        private Integer controlBehavior;
        /**
         * 是否集群限流
         */
        private Boolean clusterMode = Boolean.FALSE;
    }
}
