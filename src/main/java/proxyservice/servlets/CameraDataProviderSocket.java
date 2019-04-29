package proxyservice.servlets;

import proxyservice.App;
import proxyservice.model.cameradata.CameraDataProvider;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import proxyservice.model.cameradata.CameraDataProviderEventListener;

import java.nio.ByteBuffer;

public class CameraDataProviderSocket extends WebSocketAdapter implements CameraDataProvider {
    private CameraDataProviderEventListener eventListener;

    public CameraDataProviderSocket() {
        App.model.cameraProxy.setProvider(this);
        App.logger.log("CameraProxy Service", "Created.");
    }

    public synchronized void setEventListener(CameraDataProviderEventListener eventListener) {
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
        App.logger.log("CameraProxy Service", "Connected.");
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        if (eventListener != null) {
            eventListener.onProviderDataReceived(payload);
        }
        App.logger.log("CameraProxy Service", payload);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode,reason);
        App.logger.log("CameraProxy Service", "Closed (" + statusCode + ", " + reason + ")");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
