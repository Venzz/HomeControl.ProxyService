package proxyservice.servlets;

import proxyservice.App;
import proxyservice.model.cameradata.CameraDataConsumer;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import proxyservice.model.cameradata.CameraDataConsumerEventListener;
import proxyservice.primitives.Id;

import java.nio.ByteBuffer;

public class CameraDataConsumerSocket extends WebSocketAdapter implements CameraDataConsumer {
    private int id = Id.getNext();
    private CameraDataConsumerEventListener eventListener;

    public CameraDataConsumerSocket() {
        App.model.cameraProxy.addConsumer(this);
        App.logger.log("CameraProxy Client: " + id, "Created.");
    }

    public synchronized void setEventListener(CameraDataConsumerEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void send(byte[] data) {
        try {
            if (isNotConnected()) {
                return;
            }
            RemoteEndpoint endpoint = getRemote();
            endpoint.sendBytes(ByteBuffer.wrap(data));
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        App.logger.log("CameraProxy Client: " + id, "Connected.");
    }

    @Override
    public synchronized void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        if (eventListener != null) {
            eventListener.onConsumerDataReceived(id, payload);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        App.model.cameraProxy.removeConsumer(this);
        App.logger.log("CameraProxy Client: " + id, "Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
