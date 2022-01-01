package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.inject;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class BootSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        return super.getMethodList(mapperClass, tableInfo);
    }
}
