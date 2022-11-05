package in.hocg.boot.cps.autoconfiguration.enums;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum PlatformType implements ICode {
    TaoBao("taobao", "淘宝"),
    Jd("jd", "京东"),
    Pdd("pdd", "拼多多"),
    ;
    private final Serializable code;
    private final String name;
}
