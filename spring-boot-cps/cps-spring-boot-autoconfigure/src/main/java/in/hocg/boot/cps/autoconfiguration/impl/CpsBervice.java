package in.hocg.boot.cps.autoconfiguration.impl;

import in.hocg.boot.cps.autoconfiguration.enums.PlatformType;
import in.hocg.boot.cps.autoconfiguration.pojo.vo.PrivilegeLinkVo;

public interface CpsBervice {

    PrivilegeLinkVo getPrivilegeLink(PlatformType type, String goodId);


}
