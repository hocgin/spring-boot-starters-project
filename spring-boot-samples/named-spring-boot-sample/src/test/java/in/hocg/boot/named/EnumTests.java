package in.hocg.boot.named;

import com.google.common.base.Joiner;
import in.hocg.boot.utils.DataDictUtils;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * Created by hocgin on 2021/12/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class EnumTests {
    public static void main(String[] args) {
        List<String> sql = DataDictUtils.scanSql(Lists.newArrayList("in.hocg.boot"));
        String sqlStr = Joiner.on(System.lineSeparator()).join(sql);

        System.out.println(sqlStr);
    }
}
