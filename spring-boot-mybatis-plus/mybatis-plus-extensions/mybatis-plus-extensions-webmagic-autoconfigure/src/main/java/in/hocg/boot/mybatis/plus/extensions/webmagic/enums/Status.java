package in.hocg.boot.mybatis.plus.extensions.webmagic.enums;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum Status implements ICode {
    Progress("progress", "进行中"),
    Fail("fail", "失败"),
    Done("done", "完成");
    private final String code;
    private final String name;
}
