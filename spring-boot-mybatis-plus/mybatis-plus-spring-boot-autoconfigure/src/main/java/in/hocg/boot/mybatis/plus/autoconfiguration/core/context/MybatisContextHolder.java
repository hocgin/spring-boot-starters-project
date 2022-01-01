package in.hocg.boot.mybatis.plus.autoconfiguration.core.context;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface MybatisContextHolder {

    /**
     * 当前操作人
     *
     * @return id
     */
    default Long getUserId() {
        return null;
    }


    /**
     * 当前租户
     *
     * @return id
     */
    default Long getTenantId() {
        return null;
    }
}
