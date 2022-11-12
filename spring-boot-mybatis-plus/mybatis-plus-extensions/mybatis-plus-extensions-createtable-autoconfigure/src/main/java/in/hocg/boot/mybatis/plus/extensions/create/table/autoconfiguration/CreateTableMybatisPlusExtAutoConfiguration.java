package in.hocg.boot.mybatis.plus.extensions.create.table.autoconfiguration;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CreateTableMybatisPlusExtAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Model model = null;


        // 1. 扫描表实体
        // 2. 生成建表 SQL
        // 3. 执行 SQL
    }
}
