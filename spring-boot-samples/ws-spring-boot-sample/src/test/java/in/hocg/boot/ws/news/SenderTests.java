package in.hocg.boot.ws.news;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.utils.struct.result.Result;
import in.hocg.boot.ws.news.core.StompUtils;
import in.hocg.boot.ws.news.handler.DebugStompSessionHandler;
import in.hocg.boot.ws.sample.cmd.TestCmd;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.MimeTypeUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2022/6/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class SenderTests {
    static StompSession session;

    @BeforeAll
    public static void before() {
        session = StompUtils.getStompSession("ws://127.0.0.1:8080/.socket?ticket=hocgin");
    }

    /**
     * 测试路径变量
     */
    @Test
    @SneakyThrows
    public void testVarPath() {
        long pathVar = System.currentTimeMillis();
        String path = "/path/" + pathVar;

        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        session.subscribe("/queue" + path, new DebugStompSessionHandler(queue));

        String destination = "/app" + path;
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        session.send(headers, "{}".getBytes());

        String data = queue.poll(1, TimeUnit.SECONDS);
        log.debug(data);

        Assertions.assertTrue(data.equals(pathVar + ""));
    }

    /**
     * 消息点对点测试
     *
     * @throws InterruptedException
     */
    @Test
    public void testToUser() throws InterruptedException {
        StompSession user1 = StompUtils.getStompSession("ws://127.0.0.1:8080/.socket?ticket=hocgin");
        StompSession user2 = StompUtils.getStompSession("ws://127.0.0.1:8080/.socket?ticket=hocgin2");

        // 监听
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        user1.subscribe("/user/queue/toUser", new DebugStompSessionHandler(queue));
        user2.subscribe("/user/queue/toUser", new DebugStompSessionHandler(queue));

        // 发送
        String destination = "/app/toUser";
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        session.send(headers, "{}".getBytes());

        // 核对
        String data1 = queue.poll(1, TimeUnit.SECONDS);
        String data2 = queue.poll(1, TimeUnit.SECONDS);
        Assertions.assertNotEquals(data1, data2);
    }

    /**
     * 广播消息测试
     */
    @Test
    @SneakyThrows
    public void testBroadcast() {
        StompSession user1 = StompUtils.getStompSession("ws://127.0.0.1:8080/.socket?ticket=hocgin");
        StompSession user2 = StompUtils.getStompSession("ws://127.0.0.1:8080/.socket?ticket=hocgin2");

        // 监听
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        user1.subscribe("/queue/all", new DebugStompSessionHandler(queue));
        user2.subscribe("/queue/all", new DebugStompSessionHandler(queue));

        // 发送
        String destination = "/app/all";
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        session.send(headers, "{}".getBytes());

        // 核对
        String data1 = queue.poll(1, TimeUnit.SECONDS);
        String data2 = queue.poll(1, TimeUnit.SECONDS);
        log.debug("data: {} {}", data1, data2);

        Assertions.assertEquals(data1, data2);
    }

    /**
     * 异常测试
     */
    @Test
    @SneakyThrows
    public void testThrow() {
        String destination = "/app/throw";
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        session.subscribe("/user/queue/errors", new DebugStompSessionHandler(queue));

        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        StompSession.Receiptable result = session.send(headers, "{}".getBytes());

        String data = queue.poll(1, TimeUnit.SECONDS);

        log.debug("data: {}", data);
        Assertions.assertTrue(data.contains("测试异常"));
    }

    /**
     * 测试收到的消息
     */
    @SneakyThrows
    @Test
    public void testGetMessage() {
        String destination = "/app/get";
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        session.subscribe("/queue/get/result", new DebugStompSessionHandler(queue));

        StompHeaders headers = new StompHeaders();
        headers.set("X-Username", "hocgin");
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        String body = "{}";
        session.send(headers, body.getBytes());

        String data = queue.poll(1, TimeUnit.SECONDS);

        log.debug("data: {}", data);
        Assertions.assertEquals(body, data);
    }

    /**
     * 测试 vo / ro
     */
    @SneakyThrows
    @Test
    public void testOj() {
        String destination = "/app/obj";
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        session.subscribe("/queue/obj/result", new DebugStompSessionHandler(queue));

        StompHeaders headers = new StompHeaders();
        headers.set("X-Username", "hocgin");
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        TestCmd obj = new TestCmd().setTest("测试").setOk("hi").setBytes("Hi".getBytes());
        String body = JSONUtil.toJsonStr(obj);
        session.send(headers, body.getBytes());

        String data = queue.poll(1, TimeUnit.SECONDS);

        log.debug("data: {}", data);
        Assertions.assertTrue(JSONUtil.toBean(data, Result.class).getSuccess());
    }
}
