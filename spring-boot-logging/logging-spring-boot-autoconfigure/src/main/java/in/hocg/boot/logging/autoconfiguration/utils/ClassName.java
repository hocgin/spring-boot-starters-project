package in.hocg.boot.logging.autoconfiguration.utils;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ClassName {
    public static final String SecurityContextHoldersName = "org.springframework.security.core.context.SecurityContextHolder";

    public static boolean hasSecurityContextHolders() {
        return ClassName.isPresent(SecurityContextHoldersName);
    }

    public static boolean isPresent(String name) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
