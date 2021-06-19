package in.hocg.boot.mail.autoconfigure.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import in.hocg.boot.mail.autoconfigure.core.MailBervice;
import in.hocg.boot.mail.autoconfigure.properties.MailProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2020/10/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class MailBerviceImpl implements MailBervice, InitializingBean {
    private final MailProperties properties;
    @Getter
    private MailAccount mailAccount;

    @Override
    public void afterPropertiesSet() throws Exception {
        Integer port = properties.getPort();
        String from = properties.getFrom();
        String user = properties.getUser();
        String pass = properties.getPass();
        String host = properties.getHost();
        Boolean debug = properties.getDebug();
        Boolean auth = properties.getAuth();
        this.mailAccount = new MailAccount()
            .setAuth(auth).setDebug(debug)
            .setHost(host)
            .setUser(user).setPass(pass)
            .setFrom(from).setPort(port);
    }

    @Override
    public String sendText(String to, String subject, String content, File... files) {
        return send(to, subject, content, false, files);
    }

    @Override
    public String sendHtml(String to, String subject, String content, File... files) {
        return send(to, subject, content, true, files);
    }

    @Override
    public String send(String to, String subject, String content, boolean isHtml, File... files) {
        return send(splitAddress(to), subject, content, isHtml, files);
    }

    @Override
    public String send(String to, String cc, String bcc, String subject, String content, boolean isHtml, File... files) {
        return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, isHtml, files);
    }
    @Override
    public String sendText(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, false, files);
    }

    @Override
    public String sendHtml(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, true, files);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
        return send(tos, null, null, subject, content, isHtml, files);
    }

    @Override
    public String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
        return send(tos, ccs, bccs, subject, content, null, isHtml, files);
    }

    @Override
    public String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return send(to, subject, content, imageMap, true, files);
    }

    @Override
    public String send(String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(splitAddress(to), subject, content, imageMap, isHtml, files);
    }

    @Override
    public String send(String to, String cc, String bcc, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, imageMap, isHtml, files);
    }

    @Override
    public String sendHtml(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return send(tos, subject, content, imageMap, true, files);
    }

    @Override
    public String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(tos, null, null, subject, content, imageMap, isHtml, files);
    }

    @Override
    public String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return MailUtil.send(getMailAccount(), tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    private List<String> splitAddress(String addresses) {
        if (StrUtil.isBlank(addresses)) {
            return null;
        }

        List<String> result;
        if (StrUtil.contains(addresses, ',')) {
            result = StrUtil.splitTrim(addresses, ',');
        } else if (StrUtil.contains(addresses, ';')) {
            result = StrUtil.splitTrim(addresses, ';');
        } else {
            result = CollUtil.newArrayList(addresses);
        }
        return result;
    }
}
