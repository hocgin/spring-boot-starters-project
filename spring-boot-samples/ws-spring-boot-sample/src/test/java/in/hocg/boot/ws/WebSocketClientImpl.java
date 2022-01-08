package in.hocg.boot.ws;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by hocgin on 2022/1/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class WebSocketClientImpl extends WebSocketClient {
    public WebSocketClientImpl(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
