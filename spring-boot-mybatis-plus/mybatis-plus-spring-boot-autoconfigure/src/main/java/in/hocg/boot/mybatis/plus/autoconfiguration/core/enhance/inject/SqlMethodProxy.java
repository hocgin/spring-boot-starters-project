package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.inject;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * Created by hocgin on 2022/1/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class SqlMethodProxy extends AbstractMethod {
    private final AbstractMethod method;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return method.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }
}
