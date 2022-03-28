package in.hocg.boot.web.autoconfiguration.utils;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class SerializableUtils {

    public String toStr(Object ret) {
        String retStr;
        if ((ret instanceof InputStream) || (ret instanceof OutputStream)) {
            retStr = "[\"File Stream\"]";
        } else if ((ret instanceof ResponseEntity && ((ResponseEntity<?>) ret).getBody() instanceof Resource) || (ret instanceof Resource)) {
            retStr = "[\"Resource Stream\"]";
        } else if (Objects.isNull(ret)) {
            retStr = "[\"void\"]";
        } else {
            retStr = SerializableUtils.toJsonPrettyStr(ret);
        }
        return retStr;
    }

    public String toJsonPrettyStr(Object object) {
        if (Objects.isNull(object)) {
            return "null";
        }
        try {
            return JSONUtil.toJsonPrettyStr(object);
        } catch (Exception ignored) {
            return String.valueOf(object);
        }
    }
}
