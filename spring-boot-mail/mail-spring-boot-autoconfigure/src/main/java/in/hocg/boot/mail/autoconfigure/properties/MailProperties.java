package in.hocg.boot.mail.autoconfigure.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@ConfigurationProperties(prefix = MailProperties.PREFIX)
public class MailProperties {
    public static final String PREFIX = "boot.mail";
    /**
     * 开启状态
     */
    private Boolean enabled;
    /**
     * 邮件服务器的SMTP地址，可选，默认为smtp.<发件人邮箱后缀>
     */
    private String host;
    /**
     * 邮件服务器的SMTP端口，可选，默认25
     */
    private Integer port = 25;
    /**
     * 发件人（必须正确，否则发送失败）
     */
    private String from;
    /**
     * 用户名，默认为发件人邮箱前缀
     */
    private String user;
    /**
     * 密码（注意，某些邮箱需要为SMTP服务单独设置授权码，详情查看相关帮助）
     */
    private String pass;
    /**
     * 是否开启debug
     */
    private Boolean debug = Boolean.FALSE;
    /**
     * auth
     */
    private Boolean auth = Boolean.TRUE;

}
