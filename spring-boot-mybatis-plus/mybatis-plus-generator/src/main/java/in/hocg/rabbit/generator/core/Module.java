package in.hocg.rabbit.generator.core;

import java.util.List;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface Module {

    List<String> getIgnoreTablePrefix();

    String getPackageName();

    String getRelativePath();
}
