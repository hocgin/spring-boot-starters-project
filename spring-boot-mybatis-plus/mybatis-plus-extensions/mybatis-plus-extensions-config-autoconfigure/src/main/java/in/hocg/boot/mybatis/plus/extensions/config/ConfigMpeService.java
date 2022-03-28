package in.hocg.boot.mybatis.plus.extensions.config;

import cn.hutool.core.util.ObjectUtil;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.ScopeStructRo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.utils.ConfigHelper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ConfigMpeService {

    /**
     * 设置值
     *
     * @param scope
     * @param refId
     * @param name
     * @param value
     * @return 值ID
     */
    Optional<Long> setValue(String scope, Long refId, String name, Object value);

    Optional<Long> setValue(Long itemId, Long refId, Object value);

    void setValue(Long valueId, Object value);

    /**
     * 获取值
     *
     * @param scope
     * @param refId
     * @param name
     * @param <T>
     * @return
     */
    default <T> Optional<T> getValue(Long refId, String scope, String name) {
        Optional<ConfigScopeItemVo> opt = getScopeItem(refId, scope, name);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        ConfigScopeItemVo item = opt.get();
        return Optional.ofNullable(ConfigHelper.asValue(ObjectUtil.defaultIfNull(item.getValue(), item.getDefaultValue()), item.getType()));
    }

    /**
     * 获取域配置项
     *
     * @param scope
     * @return
     */
    Long getOrCreateScope(@NotNull String scope);

    /**
     * 更新域配置
     *
     * @param scopeId
     * @param scope
     * @param title
     * @param remark
     */
    void setScope(Long scopeId, String scope, String title, String remark);

    /**
     * 设置域结构
     *
     * @param scope
     * @param name
     * @param ro
     * @return 配置项ID
     */
    Long setScopeStruct(String scope, String name, ScopeStructRo ro);

    /**
     * 获取域结构
     *
     * @param scope
     * @return
     */
    List<ConfigScopeStructVo> getScopeStruct(Long refId, List<String> scope, Boolean readable);

    default List<ConfigScopeStructVo> getScopeStruct(Long refId, List<String> scope) {
        return getScopeStruct(refId, scope, null);
    }

    default List<ConfigScopeStructVo> getScopeStruct(List<String> scope) {
        return getScopeStruct(null, scope, null);
    }

    default Optional<ConfigScopeStructVo> getScopeStruct(Long refId, String scope) {
        return getScopeStruct(refId, List.of(scope)).stream().findFirst();
    }

    default Optional<ConfigScopeStructVo> getScopeStruct(String scope) {
        return getScopeStruct(List.of(scope)).stream().findFirst();
    }

    /**
     * 获取域配置项
     *
     * @param refId
     * @param scope
     * @param name
     * @return
     */
    Optional<ConfigScopeItemVo> getScopeItem(Long refId, String scope, String name);

    default Optional<ConfigScopeItemVo> getScopeItem(String scope, String name) {
        return getScopeItem(null, scope, name);
    }

}
