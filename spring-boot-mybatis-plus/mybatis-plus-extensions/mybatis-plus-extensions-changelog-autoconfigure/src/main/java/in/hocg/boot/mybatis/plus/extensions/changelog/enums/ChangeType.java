package in.hocg.boot.mybatis.plus.extensions.changelog.enums;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum ChangeType implements ICode {
    Modify("modify", "更新"),
    Insert("insert", "新增"),
    Delete("delete", "删除");
    private final Serializable code;
    private final String name;
}
