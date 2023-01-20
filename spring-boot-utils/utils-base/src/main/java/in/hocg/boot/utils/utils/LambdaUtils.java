package in.hocg.boot.utils.utils;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.PropertyNamer;
import in.hocg.boot.utils.lambda.SFunction;
import in.hocg.boot.utils.lambda.SerializedLambda;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class LambdaUtils {

    /**
     * 获取方法名称
     *
     * @param lambda lambda
     * @return 方法名称
     */
    public <T> String getMethodName(SFunction<T, ?> lambda) {
        return SerializedLambda.resolve(lambda).getImplMethodName();
    }

    /**
     * 获取属性名称
     *
     * @param lambda lambda
     * @return 属性名称
     */
    public <T> String getPropertyName(SFunction<T, ?> lambda) {
        return PropertyNamer.methodToProperty(getMethodName(lambda));
    }

    /**
     * 获取数据库字段名
     *
     * @param lambda lambda
     * @return 数据库字段名
     */
    public <T> String getColumnName(SFunction<T, ?> lambda) {
        return StrUtil.toUnderlineCase(getPropertyName(lambda));
    }

}
