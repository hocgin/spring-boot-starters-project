package in.hocg.boot.web.jackson.sensitive;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public enum SensitiveType {
    /**
     * 自定义
     */
    CUSTOMER,
    /**
     * 用户名, 马*腾, 马*
     */
    CHINESE_NAME,
    /**
     * 身份证号, 110110********1234
     */
    ID_CARD,
    /**
     * 座机号, ****1234
     */
    TELEPHONE,
    /**
     * 手机号, 136****7016
     */
    MOBILE_PHONE,
    /**
     * 地址, 北京********
     */
    ADDRESS,
    /**
     * 电子邮件, 1*****1@xx.xxx
     */
    EMAIL,
    /**
     * 银行卡, 111111************1111
     */
    BANK_CARD,
    /**
     * 密码, ******
     */
    PASSWORD,
    /**
     * 密钥, ******
     */
    KEY
}
