package in.hocg.boot.ws;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class WebSocketTests  {

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClientImpl client = new WebSocketClientImpl(new URI("ws://127.0.0.1:8080/ws"));

        client.run();
        System.out.println("client.run()");
    }
}
