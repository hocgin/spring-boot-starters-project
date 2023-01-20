package in.hocg.boot.utils.utils;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class SensitiveUtils {

    /**
     * 字符串脱敏
     *
     * @param str              原字符串
     * @param prefixNoMaskSize 左侧保留几位明文
     * @param suffixNoMaskSize 右侧保留几位明文
     * @param mask             遮罩的字符串, 如'*'
     * @return 脱敏后结果
     */
    public static String custom(String str, int prefixNoMaskSize, int suffixNoMaskSize, String mask) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = str.length(); i < n; i++) {
            if (i < prefixNoMaskSize) {
                sb.append(str.charAt(i));
                continue;
            }
            if (i > (n - suffixNoMaskSize - 1)) {
                sb.append(str.charAt(i));
                continue;
            }
            sb.append(mask);
        }
        return sb.toString();
    }

    /**
     * 脱敏中文姓名。马*腾, 马*
     *
     * @param str 姓名
     * @return s
     */
    public static String chineseName(String str) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        return custom(str, 0, 1, "*");
    }

    /**
     * 脱敏身份证号。显示前六位, 后四位
     *
     * @param str 身份证号码
     * @return s
     */
    public static String idCardNum(String str) {
        return custom(str, 6, 4, "*");
    }

    /**
     * 脱敏座机号。****1234
     *
     * @param str 固定电话
     * @return s
     */
    public static String telephone(String str) {
        return custom(str, 0, 4, "*");
    }

    /**
     * 脱敏手机号码。135****6810
     *
     * @param str 手机号码
     * @return s
     */
    public static String mobilePhone(String str) {
        return custom(str, 3, 4, "*");
    }

    /**
     * 脱敏地址。北京市海淀区****
     *
     * @param str 地址
     * @return s
     */
    public static String address(String str) {
        return custom(str, 6, 0, "*");
    }

    /**
     * 脱敏电子邮箱。d**@126.com
     *
     * @param str 电子邮箱
     * @return s
     */
    public static String email(String str) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        int index = StrUtil.indexOf(str, '@');
        if (index <= 1) {
            return str;
        }
        String preEmail = custom(str.substring(0, index), 1, 0, "*");
        return preEmail + str.substring(index);

    }

    /**
     * 脱敏银行卡号。前六位，后四位。622260**********1234
     *
     * @param str 银行卡号
     * @return s
     */
    public static String bankCard(String str) {
        return custom(str, 6, 4, "*");
    }

    /**
     * 脱敏密码。******
     *
     * @param str 密码
     * @return s
     */
    public static String password(String str) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        return "******";
    }

    /**
     * 脱敏密钥。***xdS
     *
     * @param key 密钥
     * @return 结果
     */
    public static String key(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }
        int viewLength = 6;
        StringBuilder tmpKey = new StringBuilder(Objects.requireNonNull(custom(key, 0, 3, "*")));
        if (tmpKey.length() > viewLength) {
            return tmpKey.substring(tmpKey.length() - viewLength);
        } else if (tmpKey.length() < viewLength) {
            int buffLength = viewLength - tmpKey.length();
            for (int i = 0; i < buffLength; i++) {
                tmpKey.insert(0, "*");
            }
            return tmpKey.toString();
        } else {
            return tmpKey.toString();
        }
    }
}
